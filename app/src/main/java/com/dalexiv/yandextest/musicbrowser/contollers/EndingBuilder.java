package com.dalexiv.yandextest.musicbrowser.contollers;

/**
 * Created by dalexiv on 4/24/16.
 */
public class EndingBuilder {
    public static String buildStats(int albums, int tracks, String delimeter,
                                     String[] albumsEnding, String[] tracksEnding) {
        String constructedString = albums + " " + buildEnding(albums, albumsEnding);
        constructedString += delimeter + tracks + " " + buildEnding(tracks, tracksEnding);
        return constructedString;
    }

    public static String buildEnding(int labels, String[] endings) {
        if (endings.length != 3)
            throw new IllegalArgumentException("Wrong number of ending passed to method");

        String ending;
        int scaledLabels = labels % 100;

        if (scaledLabels / 10 % 10 != 1 && scaledLabels % 10 == 1)
            ending = endings[0];
        else if (scaledLabels / 10 % 10 != 1
                && scaledLabels % 10 >= 2 && scaledLabels % 10 <= 4)
            ending = endings[1];
        else
            ending = endings[2];
        return ending;
    }
}
