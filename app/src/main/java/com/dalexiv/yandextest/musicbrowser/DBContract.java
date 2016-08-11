package com.dalexiv.yandextest.musicbrowser;

/**
 * Created by dalexiv on 8/10/16.
 */

public interface DBContract {
    String DB_NAME = "main.sqlite";

    String GROUPED_GENRES = "genres";

    String PERFORMERS = "performers";
    interface Performers {
        String ID = "rowid";
        String PERFORMER_NAME = "name";
        String ALBUMS = "albums";
        String TRACKS = "tracks";
        String LINK = "link";
        String DESCRIPTION = "desc";
        String COVER_BIG = "cover_big";
        String COVER_SMALL = "cover_small";
    }

    String PERFORMERS_GENRE = "performers_genre";
    interface PerformersGenres {
        String ID = "performer_genre_id";
        String PERFORMERS = "performer_id";
        String GENRE = "genre_id";
    }

    String GENRES = "genres";
    interface Genres {
        String ID = "genre_id";
        String GENRE_NAME = "genre_name";
    }
}
