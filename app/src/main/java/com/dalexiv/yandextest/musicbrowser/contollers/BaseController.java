package com.dalexiv.yandextest.musicbrowser.contollers;

import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.R;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Base class for both activity contollers
    Encapsulates logic of generating and setting data
 */
public class BaseController {
    // Refernce to acitivty
    protected Context context;

    // Constants from context
    protected String[] albumsEnding;
    protected String[] tracksEnding;

    public BaseController(Context context) {
        this.context = context;
        albumsEnding = context.getResources().getStringArray(R.array.albums);
        tracksEnding = context.getResources().getStringArray(R.array.tracks);
    }
}
