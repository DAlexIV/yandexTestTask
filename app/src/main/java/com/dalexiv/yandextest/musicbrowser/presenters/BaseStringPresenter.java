package com.dalexiv.yandextest.musicbrowser.presenters;

import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Base class for both activity controllers
    Encapsulates logic of generating and setting data
 */
public class BaseStringPresenter<T extends Fragment> extends Presenter<T> {
    // Weak reference to activity (for correct gc)
    protected StringBuilder builder;
    protected Resources res;

    @Override
    public void bindView(@NonNull T view) {
        super.bindView(view);
        res = view.getResources();
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

    @Override
    public void unbindView(@NonNull T view) {
        super.unbindView(view);
        res = null;
        builder = null;
    }
}
