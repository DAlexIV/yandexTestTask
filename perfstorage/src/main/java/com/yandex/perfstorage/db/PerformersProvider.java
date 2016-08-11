package com.yandex.perfstorage.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yandex.perfstorage.di.DaggerProviderComponent;
import com.yandex.perfstorage.di.IPerformer;
import com.yandex.perfstorage.di.NetModule;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

/**
 * Created by dalexiv on 8/10/16.
 */

public class PerformersProvider extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "com.yandex.perfstorage";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(DBContract.DB_NAME).build();

    public static final int PERFORMER = 1;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    @Inject
    IPerformer iPerformer;

    private DBOpenHelper database;
    private DBBackend backend;

    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, DBContract.PERFORMERS, PERFORMER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        DaggerProviderComponent
                .builder()
                .netModule(new NetModule())
                .build()
                .inject(this);

        database = new DBOpenHelper(getContext());
        backend = new DBBackend(database);

        if (backend.getAllPerformers(database.getReadableDatabase()) == null) {
            // TODO store subs
            iPerformer.getPerformers()
                    .subscribeOn(Schedulers.io())
                    .subscribe(performers -> backend.insertPerformers(
                            database.getWritableDatabase(), performers),
                            error -> Log.e("PROVIDER", "ContentProvide::onCreate", error));
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return backend.getAllPerformers(database.getReadableDatabase());
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // Just loading from background, skip
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PERFORMER:
                return "vnd.android.cursor.dir/vnd.com.yandex.perfstorage.db";
        }
        return null;
    }


    @Override
    public int delete(Uri uri, String s, String[] strings) {
        // Stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        // Stub
        return 0;
    }
}
