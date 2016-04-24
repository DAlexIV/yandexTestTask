package com.dalexiv.yandextest.musicbrowser.contollers;

import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.R;

/**
 * Created by dalexiv on 4/24/16.
 */
public class BaseController {
    protected Context context;

    // Constants
    protected String[] albumsEnding;
    protected String[] tracksEnding;

    public BaseController(Context context) {
        this.context = context;
        albumsEnding = context.getResources().getStringArray(R.array.albums);
        tracksEnding = context.getResources().getStringArray(R.array.tracks);
    }
}
