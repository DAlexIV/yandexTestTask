package com.dalexiv.yandextest.musicbrowser.contollers;

import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

import java.util.Arrays;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Encapsulates logic of generating and setting data at DetailedActivity
 */
public class DetailedController extends BaseController {
    private Performer performer;

    public DetailedController(Performer performer, Context context) {
        super(context);
        this.performer = performer;
    }

    public String generateGenres() {
        return Arrays.toString(performer.getGenres()).replaceAll("[\\[\\]]", "");
    }

    public String generateStats() {
        return EndingBuilder.buildStats(performer.getAlbums(),
                performer.getTracks(), " · ", albumsEnding, tracksEnding);
    }

    public String generateDescription() {
        return performer.getName() + " – " + performer.getDescription();
    }

    public String generateLink() {
            return "Узнайте больше на\n " + performer.getLink();
    }
}
