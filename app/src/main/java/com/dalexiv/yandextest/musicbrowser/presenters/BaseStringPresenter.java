package com.dalexiv.yandextest.musicbrowser.presenters;

import android.app.Fragment;
import android.content.res.Resources;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

import java.lang.ref.WeakReference;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Base class for both activity controllers
    Encapsulates logic of generating and setting data
 */
public class BaseStringPresenter {
    // Weak reference to activity (for correct gc)
    protected WeakReference<Fragment> fragment;
    protected StringBuilder builder;
    protected Resources res;

    public BaseStringPresenter(Fragment fragment) {
        this.fragment = new WeakReference<>(fragment);
        res = fragment.getResources();
        builder = new StringBuilder();
    }

    public String generateStats(Performer performer, String splitter) {
        builder.setLength(0);
        builder.append(performer.getAlbums());
        builder.append(" ");
        builder.append(res.getQuantityString(R.plurals.albums, performer.getAlbums()));
        builder.append(splitter);
        builder.append(performer.getTracks());
        builder.append(" ");
        builder.append(res.getQuantityString(R.plurals.tracks, performer.getTracks()));
        return builder.toString();
    }


}
