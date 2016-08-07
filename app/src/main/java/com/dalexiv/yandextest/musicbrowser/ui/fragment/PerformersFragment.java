package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.di.FragmentInjectors;
import com.dalexiv.yandextest.musicbrowser.net.DiskCache;
import com.dalexiv.yandextest.musicbrowser.net.IPerformer;
import com.dalexiv.yandextest.musicbrowser.ui.DividerItemDecoration;
import com.dalexiv.yandextest.musicbrowser.ui.PerformersAdapter;
import com.trello.rxlifecycle.components.RxFragment;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dalexiv on 7/18/16.
 */

public class PerformersFragment extends RxFragment {
    private static final String TAG = PerformersFragment.class.getSimpleName();

    // Constants
    private static final long ANIMATION_DURATION = 750;
    private static final int NUMBER_OF_ANIMATED_ITEMS = 5;

    // Layout
    @BindView(R.id.recyclerPerfs)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Unbinder unbinder;

    private PerformersAdapter pAdapter;

    @Inject
    IPerformer iPerformer;

    @Inject
    DiskCache cache;

    // Observer for UI update
    private Observer<Performer> showResultObserver;

    // Custom observable for better animation timings
    private Observable<Long> custromInterval;

    // Main subsription
    private Subscription netCall;

    // Observable with cache
    private Observable<ArrayList<Performer>> cacheObservable;

    public static PerformersFragment newInstance() {
        return new PerformersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentInjectors.inject(this);
        cacheObservable = Observable.fromCallable(() -> cache.restoreFromDisk());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_performers, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Refactor into interface
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Исполнители");
        }

        configureRecyclerViewAndAdapter();
        setupSwipeToRefresh();

        initRx();
        loadFromCacheAndNetwork();
    }

    private void setupSwipeToRefresh() {
        // Clearing old data and loading new one from network
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            pAdapter.clearPerformers();
            loadFromNetworkOnly();
        });

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                Color.YELLOW, ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    private void configureRecyclerViewAndAdapter() {
        // Initializing adapter
        pAdapter = new PerformersAdapter(this);

        // Configuring recyclerview
        RecyclerView.LayoutManager mLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(pAdapter);
    }

    private void initRx() {
        showResultObserver = new Observer<Performer>() {
            @Override
            public void onCompleted() {
                setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                setRefreshing(false);

                if (e instanceof ConnectException || e instanceof UnknownHostException)
                    Snackbar.make(mRecyclerView, "No internet connection, swipe to refresh",
                            Snackbar.LENGTH_LONG).show();
                if (e instanceof SocketTimeoutException)
                    Snackbar.make(mRecyclerView, "Connection is a little bit slow, swipe to refresh",
                            Snackbar.LENGTH_LONG).show();
                else // In some unpredictable case
                    Snackbar.make(mRecyclerView, e.toString(), Snackbar.LENGTH_LONG).show();

                e.printStackTrace();
            }

            @Override
            public void onNext(Performer performer) {
                pAdapter.addPerformer(performer);
            }
        };

        // Calculating animation values
        long ITEM_ANIMATION_DURATION = ANIMATION_DURATION / NUMBER_OF_ANIMATED_ITEMS;

        custromInterval = Observable.interval(ITEM_ANIMATION_DURATION, TimeUnit.MILLISECONDS).flatMap(time -> {
            if (time < NUMBER_OF_ANIMATED_ITEMS) // Take first with animation interval
                return Observable.just(time);
            else if (time == NUMBER_OF_ANIMATED_ITEMS) // Then emit others almost as one
                return Observable.interval(1, TimeUnit.MILLISECONDS);
            else // Finally, just return nothing
                return Observable.empty();
        });
    }

    private void loadFromCacheAndNetwork() {
        // Show that we are loading smthing
        setRefreshing(true);

        // Rx magic goes there
        netCall = Observable.zip(Observable.concat(cacheObservable, iPerformer.getPerformers())
                .first()
                .doOnNext(performers -> cache.saveToDisk(performers))
                .flatMap(Observable::from), custromInterval, (data, delay) -> data)
                .compose(bindToLifecycle())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }

    private void loadFromNetworkOnly() {
        // Show that we are loading smthing
        setRefreshing(true);

        // Another sort of Rx magic (without cache)
        netCall = Observable.zip(iPerformer.getPerformers()
                        .doOnNext(performers -> cache.saveToDisk(performers))
                        .flatMap(Observable::from),
                custromInterval, (data, delay) -> data)
                .compose(bindToLifecycle())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setRefreshing(boolean isRefreshing) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.post(() -> {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(isRefreshing);
            });
    }
}
