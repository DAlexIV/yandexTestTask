package com.dalexiv.yandextest.musicbrowser.presenters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dalexiv.yandextest.musicbrowser.comms.DbConnection;
import com.dalexiv.yandextest.musicbrowser.comms.DiskCache;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.di.PresenterInjectors;
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
    private static final String TAG = PerformersStringPresenter.class.getSimpleName();
    private static final Uri CONTENT_URI
            = Uri.parse("content://com.yandex.perfstorage/main.sqlite/1");

    // Current list of performers in adapter
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
        loadFromCacheAndDb();
    }

    private void loadFromCacheAndDb() {
        // TODO make a db call

        // Show that we are loading smthing
        view().setRefreshing(true);

        // Rx magic goes there
        final Observable<Performer> loadingObservable = Observable.zip(
                Observable.concat(cacheObservable
                        .filter(cache -> cache != null),
                        DbConnection.observe(view().getActivity()
                                .getContentResolver().query(CONTENT_URI, null, null, null, null))
                                .doOnNext(performers -> cache.saveToDisk(performers)))
                        .first()
                        .flatMap(Observable::from),
                view().getAnimationIntervalObservable(),
                (data, delay) -> data);

        netCall = applySchedulers(loadingObservable)
                .subscribe(showResultObserver);
    }

// Legacy code
//    private void loadFromNetworkOnly() {
//
//        // Show that we are loading smthing
//        view().setRefreshing(true);
//
//        // Another sort of Rx magic (without cache)
//        Observable<Performer> loadingObservable =
//                Observable.zip(
//                        iPerformer.getPerformers()
//                                .doOnNext(performers -> cache.saveToDisk(performers))
//                                .flatMap(Observable::from),
//                        view().getAnimationIntervalObservable(),
//                        (data, delay) -> data);
//
//        netCall = applySchedulers(loadingObservable)
//                .subscribe(showResultObserver);
//    }

    private Observable<Performer> applySchedulers(Observable<Performer> observable) {
        return observable.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

                if (e instanceof ConnectException || e instanceof UnknownHostException
                        || e instanceof NullPointerException)
                    view().notifyUser("No internet connection, swipe to refresh");
                else if (e instanceof SocketTimeoutException)
                    view().notifyUser("Connection is a little bit slow, swipe to refresh");
                else // In some unpredictable case
                    view().notifyUser(e.toString());

                e.printStackTrace();

                Log.e(TAG, "initRx:: onError", e);
            }

            @Override
            public void onNext(Performer performer) {
                view().addPerformer(performer);
            }
        };
    }


    public void doSwipeToRefresh() {
        view().clearPerformers();
        view().setRefreshing(false);
//        loadFromNetworkOnly();
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
