package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

import rx.Observable;

/**
 * Created by dalexiv on 8/8/16.
 */

public interface IDisplayPerformers {
    Observable<Long> getAnimationIntervalObservable();
    void setRefreshing(boolean isRefreshing);
    void notifyUser(String message);
    void addPerformer(Performer performer);
    void clearPerformers();
}
