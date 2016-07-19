package com.dalexiv.yandextest.musicbrowser.domain;

import android.app.Fragment;

import com.dalexiv.yandextest.musicbrowser.R;

import java.lang.ref.WeakReference;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Base class for both activity controllers
    Encapsulates logic of generating and setting data
 */
public class BaseController {
    // Weak reference to activity (for correct gc)
    protected WeakReference<Fragment> fragment;

    // Constants from fragment
    protected String[] albumsEnding;
    protected String[] tracksEnding;

    public BaseController(Fragment fragment) {
        this.fragment = new WeakReference<>(fragment);
        albumsEnding = fragment.getResources().getStringArray(R.array.albums);
        tracksEnding = fragment.getResources().getStringArray(R.array.tracks);
    }
}
