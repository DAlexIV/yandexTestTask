package com.dalexiv.yandextest.musicbrowser.presenters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.di.PresenterInjectors;
import com.dalexiv.yandextest.musicbrowser.net.DiskCache;
import com.dalexiv.yandextest.musicbrowser.net.IPerformer;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.PerformersFragment;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Encapsulates logic of generating and setting data at PerformersAdapter
 */
public class PerformersStringPresenter extends BaseStringPresenter<PerformersFragment> {
    // Current list of performers in adapter
    @Inject
    IPerformer iPerformer;
    @Inject
    DiskCache cache;

    // Observable with cache
    private Observable<ArrayList<Performer>> cacheObservable;

    // Observer for UI update
    private Observer<Performer> showResultObserver;

    // Main subsription
    private Subscription netCall;

    @Override
    public void bindView(@NonNull PerformersFragment view) {
        super.bindView(view);
        PresenterInjectors.inject(this);
        cacheObservable = Observable.fromCallable(() -> cache.restoreFromDisk());
        initRx();
        loadFromCacheAndNetwork();
    }

    private void loadFromCacheAndNetwork() {
        // Show that we are loading smthing
        view().setRefreshing(true);

        // Rx magic goes there
        netCall = Observable.zip(Observable.concat(cacheObservable, iPerformer.getPerformers())
                .first()
                .doOnNext(performers -> cache.saveToDisk(performers))
                .flatMap(Observable::from), view().getAnimationIntervalObservable(), (data, delay) -> data)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }


    private void loadFromNetworkOnly() {
        // Show that we are loading smthing
        view().setRefreshing(true);

        // Another sort of Rx magic (without cache)
        netCall = Observable.zip(iPerformer.getPerformers()
                        .doOnNext(performers -> cache.saveToDisk(performers))
                        .flatMap(Observable::from),
                view().getAnimationIntervalObservable(), (data, delay) -> data)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showResultObserver);
    }

    private void initRx() {
        showResultObserver = new Observer<Performer>() {
            @Override
            public void onCompleted() {
                view().setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                view().setRefreshing(false);

                if (e instanceof ConnectException || e instanceof UnknownHostException)
                    view().notifyUser("No internet connection, swipe to refresh");
                if (e instanceof SocketTimeoutException)
                    view().notifyUser("Connection is a little bit slow, swipe to refresh");
                else // In some unpredictable case
                    view().notifyUser(e.toString());

                e.printStackTrace();
            }

            @Override
            public void onNext(Performer performer) {
                view().addPerformer(performer);
            }
        };
    }

    public void doSwipeToRefresh() {
        view().clearPerformers();
        loadFromNetworkOnly();
    }

    public Fragment getView() {
        return view();
    }

    @Override
    public void unbindView(@NonNull PerformersFragment view) {
        super.unbindView(view);
        netCall.unsubscribe();
    }
}
