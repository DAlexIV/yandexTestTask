package com.yandex.perfstorage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yandex.perfstorage.dataModel.Performer;

import java.util.ArrayList;
import java.util.List;

import static com.yandex.perfstorage.db.DBContract.PerformersGenres.PERFORMERS;

/**
 * Created by dalexiv on 8/10/16.
 */

public class DBBackend {
    private final DBOpenHelper dbOpenHelper;

    public DBBackend(DBOpenHelper dbOpenHelper) {
        this.dbOpenHelper = dbOpenHelper;
    }

    public Cursor getAllPerformers(SQLiteDatabase db) {
        String joinQuery = DBContract.PERFORMERS + " LEFT JOIN " + DBContract.PERFORMERS_GENRE
                + " ON " + DBContract.PERFORMERS + "." + DBContract.Performers.ID + "="
                + DBContract.PERFORMERS_GENRE + "." + DBContract.PerformersGenres.PERFORMERS
                + " INNER JOIN " + DBContract.GENRES
                + " ON " + DBContract.PERFORMERS_GENRE + "." + DBContract.PerformersGenres.GENRE + "="
                + DBContract.GENRES + "." + DBContract.Genres.ID
                + " GROUP BY " + DBContract.Performers.ID;
        String[] columns = new String[]{
                DBContract.Performers.ID,
                DBContract.Performers.PERFORMER_NAME,
                "GROUP_CONCAT(" + DBContract.GENRES + "."
                        + DBContract.Genres.ID + ") AS " + DBContract.GROUPED_GENRES,
                DBContract.Performers.ALBUMS,
                DBContract.Performers.TRACKS,
                DBContract.Performers.LINK,
                DBContract.Performers.DESCRIPTION,
                DBContract.Performers.COVER_SMALL,
                DBContract.Performers.COVER_BIG};
        return db.query(joinQuery, columns, null, null, null, null, null);
    }

    public void insertPerformers(SQLiteDatabase db, List<Performer> performers) {
        for (Performer performer : performers) {
            insertPerformer(db, performer);
        }
    }

    private void insertPerformer(SQLiteDatabase db, Performer performer) {
        long insertPerfId = insertOnlyPerformer(db, performer);
        List<Long> insertedGenres = new ArrayList<>();
        for (String genre : performer.getGenres()) {
            insertedGenres.add(insertOnlyGenre(db, genre));
        }
        insertGenrePerformer(db, insertPerfId, insertedGenres);
    }

    private long insertOnlyGenre(SQLiteDatabase db, String genre) {
        ContentValues values = new ContentValues();
        values.put(DBContract.Genres.GENRE_NAME, genre);
        return db.insertWithOnConflict(DBContract.GENRES, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    private long insertOnlyPerformer(SQLiteDatabase db, Performer performer) {
        ContentValues values = new ContentValues();
        values.put(DBContract.Performers.PERFORMER_NAME, performer.getName());
        values.put(DBContract.Performers.ALBUMS, performer.getAlbums());
        values.put(DBContract.Performers.TRACKS, performer.getTracks());
        values.put(DBContract.Performers.LINK, performer.getLink());
        values.put(DBContract.Performers.DESCRIPTION, performer.getDescription());
        values.put(DBContract.Performers.COVER_SMALL, performer.getCover().getSmall());
        values.put(DBContract.Performers.COVER_BIG, performer.getCover().getBig());
        return db.insert(DBContract.PERFORMERS, null, values);
    }

    private void insertGenrePerformer(SQLiteDatabase db, long performerId, List<Long> genres) {
        ContentValues values = new ContentValues();
        for (Long genre : genres) {
            values.put(DBContract.PerformersGenres.GENRE, genre);
            values.put(PERFORMERS, performerId);
            db.insert(DBContract.PERFORMERS_GENRE, null, values);
        }
    }
}
