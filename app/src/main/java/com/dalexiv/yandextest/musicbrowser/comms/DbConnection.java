package com.dalexiv.yandextest.musicbrowser.comms;

import android.database.Cursor;

import com.dalexiv.yandextest.musicbrowser.DBContract;
import com.dalexiv.yandextest.musicbrowser.dataModel.Cover;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by dalexiv on 8/11/16.
 */

public class DbConnection {
    public static List<Performer> mapCursorToList(Cursor cursor) {
        List<Performer> dbList = new ArrayList<>();
        if (cursor != null)
            cursor.moveToFirst();
        do {
            dbList.add(new Performer(cursor.getInt(cursor.getColumnIndex(DBContract.Performers.ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Performers.PERFORMER_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBContract.GROUPED_GENRES)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.Performers.ALBUMS)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.Performers.TRACKS)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Performers.LINK)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Performers.DESCRIPTION)),
                    new Cover(
                            cursor.getString(cursor.getColumnIndex(DBContract.Performers.COVER_SMALL)),
                            cursor.getString(cursor.getColumnIndex(DBContract.Performers.COVER_BIG)))));

        } while (cursor.moveToNext());
        return dbList;
    }

    public static Observable<List<Performer>> observe(Cursor cursor) {
        return Observable.from(mapCursorToList(cursor)).toList();
    }
}
