package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.contoller.PerformersAdapter;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.retro.IPerformer;
import com.dalexiv.yandextest.musicbrowser.retro.RetrofitHolder;
import com.dalexiv.yandextest.musicbrowser.ui.DividerItemDecoration;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {
    // Layout
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    PerformersAdapter pAdapter;

    // Retro
    IPerformer iPerformer;

    // Rx
    Observer<Performer> showResultObserver;
    Observable<Long> custromInterval;
    Subscription netCall;

    // Cache
    Observable<ArrayList<Performer>> cacheObservable;
    ArrayList<Performer> cache;

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerPerfs);

        configureRecyclerViewAndAdapter();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            pAdapter.clearPerformers();
            loadFromNetworkOnly();
        });

        iPerformer = RetrofitHolder.getRetrofit()
                .create(IPerformer.class);

        initRx();
        loadFromCacheAndNetwork();
    }

    private void configureRecyclerViewAndAdapter() {
        pAdapter = new PerformersAdapter(MainActivity.this);
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
//                if (!(e instanceof MissingBackpressureException))
                    Snackbar.make(mRecyclerView, "No internet connection, swipe to refresh",
                            Snackbar.LENGTH_LONG).show();
                mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onNext(Performer performer) {
                pAdapter.addPerformer(performer);

                // Cache data
                cache.add(performer);
            }
        };

        custromInterval = Observable.interval(100, TimeUnit.MILLISECONDS).flatMap(time -> {
            if (time < 10)
                return Observable.just(time);
            else if (time == 10)
                return Observable.interval(1, TimeUnit.MILLISECONDS);
            else
                return Observable.empty();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("cache", cache);
    }

    private void loadFromCacheAndNetwork() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));

        netCall = Observable.zip(cacheObservable.switchIfEmpty(Observable.defer(()
                        -> iPerformer.getPerformers())).flatMap(Observable::from),
                custromInterval, (data, delay) -> data)
                .compose(bindToLifecycle())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }

    private void loadFromNetworkOnly() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        netCall = Observable.zip(iPerformer.getPerformers().flatMap(Observable::from),
                custromInterval, (data, delay) -> data)
                .compose(bindToLifecycle())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }
}
