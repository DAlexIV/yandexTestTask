package com.dalexiv.yandextest.musicbrowser.presenters;

import android.app.Fragment;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

import java.util.Arrays;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Encapsulates logic of generating and setting data at DetailedFragment
 */
public class DetailedStringPresenter extends BaseStringPresenter {
    private Performer performer;

    public DetailedStringPresenter(Performer performer, Fragment fragment) {
        super(fragment);
        this.performer = performer;
    }

    public String generateGenres() {
        return Arrays.toString(performer.getGenres()).replaceAll("[\\[\\]]", "");
    }

    public String generateStats() {
        return super.generateStats(performer, " \u2022 ");
    }

    public String generateDescription() {
        builder.setLength(0);
        builder.append(performer.getName());
        builder.append(" - ");
        builder.append(performer.getDescription());
        return builder.toString();
    }

    public String generateLink() {
            return "Узнайте больше на\n " + performer.getLink();
    }
}
