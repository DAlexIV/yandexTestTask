package com.yandex.perfstorage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.yandex.perfstorage.db.DBContract.DB_NAME;
import static com.yandex.perfstorage.db.DBContract.GENRES;
import static com.yandex.perfstorage.db.DBContract.PERFORMERS;
import static com.yandex.perfstorage.db.DBContract.PERFORMERS_GENRE;

/**
 * Created by dalexiv on 8/10/16.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PERFORMERS + "(" +
                DBContract.Performers.ID + " INTEGER PRIMARY KEY, " +
                DBContract.Performers.PERFORMER_NAME + " TEXT UNIQUE NOT NULL, " +
                DBContract.Performers.ALBUMS + " INTEGER, " +
                DBContract.Performers.TRACKS + " INTEGER, " +
                DBContract.Performers.LINK + " TEXT, " +
                DBContract.Performers.DESCRIPTION + " TEXT, " +
                DBContract.Performers.COVER_SMALL + " TEXT NOT NULL, " +
                DBContract.Performers.COVER_BIG + " TEXT NOT NULL " +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + GENRES + "(" +
            DBContract.Genres.ID + " INTEGER PRIMARY KEY, " +
                DBContract.Genres.GENRE_NAME + " TEXT UNIQUE NOT NULL " +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PERFORMERS_GENRE + "(" +
                DBContract.PerformersGenres.ID + " INTEGER PRIMARY KEY, " +
                DBContract.PerformersGenres.PERFORMERS + " INTEGER, " +
                DBContract.PerformersGenres.GENRE + " INTEGER " +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Updating for dev versions!
        db.execSQL("DROP TABLE " + PERFORMERS_GENRE);
        db.execSQL("DROP TABLE " + PERFORMERS);
        db.execSQL("DROP TABLE " + GENRES);
        onCreate(db);
    }
}
