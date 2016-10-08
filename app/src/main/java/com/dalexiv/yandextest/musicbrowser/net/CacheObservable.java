package com.dalexiv.yandextest.musicbrowser.net;

import rx.Observable;

/**
 * Created by dalexiv on 8/2/16.
 */

// TODO how to make custom observable?
public class CacheObservable extends Observable{
    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f {@link OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected CacheObservable(OnSubscribe f) {
        super(f);
    }
}
