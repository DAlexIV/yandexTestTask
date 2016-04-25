package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.PerformersAdapter;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.retro.IPerformer;
import com.dalexiv.yandextest.musicbrowser.retro.RetrofitHolder;
import com.dalexiv.yandextest.musicbrowser.ui.DividerItemDecoration;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
    Activity with performers preview
 */
public class MainActivity extends RxAppCompatActivity {
    // Layout
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    PerformersAdapter pAdapter;

    // Retro
    IPerformer iPerformer;

    // Observer for UI update
    Observer<Performer> showResultObserver;

    // Custom observable for better animation timings
    Observable<Long> custromInterval;

    // Main subsription
    Subscription netCall;

    // Observable with cache
    Observable<ArrayList<Performer>> cacheObservable;

    // Cache for saving
    ArrayList<Performer> cache;

    // Constants
    private final long ANIMATION_DURATION = 750;
    private final int NUMBER_OF_ANIMATED_ITEMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Исполнители");

        // Restoring data from cache
        cache = new ArrayList<>();
        if (savedInstanceState != null)
            cacheObservable = Observable.just(savedInstanceState.getParcelableArrayList("cache"));
        else
            cacheObservable = Observable.empty();

        // Initializing views (Butterknife for wimps)
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerPerfs);

        configureRecyclerViewAndAdapter();
        setupSwipeToRefresh();

        // Creating retrofit interface
        iPerformer = RetrofitHolder.getRetrofit()
                .create(IPerformer.class);

        initRx();
        loadFromCacheAndNetwork();
    }

    private void setupSwipeToRefresh() {
        // Clearing old data and loading new one from network
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            pAdapter.clearPerformers();
            loadFromNetworkOnly();
        });

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary),
                Color.YELLOW, ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void configureRecyclerViewAndAdapter() {
        // Initializing adapter
        pAdapter = new PerformersAdapter(MainActivity.this);

        // Configuring recyclerview
        RecyclerView.LayoutManager mLayoutManager
                = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(pAdapter);
    }

    private void initRx() {
        showResultObserver = new Observer<Performer>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ConnectException || e instanceof UnknownHostException)
                    Snackbar.make(mRecyclerView, "No internet connection, swipe to refresh",
                            Snackbar.LENGTH_LONG).show();
                else // In some unpredictable case
                    Snackbar.make(mRecyclerView, e.getMessage(), Snackbar.LENGTH_LONG).show();

                // Anyway show that we are done
                mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onNext(Performer performer) {
                pAdapter.addPerformer(performer);

                // Cache data
                cache.add(performer);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saving cache
        outState.putParcelableArrayList("cache", cache);
    }

    private void loadFromCacheAndNetwork() {
        // Show that we are loading smthing
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));

        // Rx magic goes there
        netCall = Observable.zip(cacheObservable
                        .switchIfEmpty(Observable.defer(() -> iPerformer.getPerformers()))
                        .flatMap(Observable::from),
                custromInterval, (data, delay) -> data)
                .compose(bindToLifecycle())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }

    private void loadFromNetworkOnly() {
        // Show that we are loading smthing
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));

        // Another sort of Rx magic (without cache)
        netCall = Observable.zip(iPerformer.getPerformers()
                        .flatMap(Observable::from),
                custromInterval, (data, delay) -> data)
                .compose(bindToLifecycle())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }
}
