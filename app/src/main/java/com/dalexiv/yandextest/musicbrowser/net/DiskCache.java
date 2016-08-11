package com.dalexiv.yandextest.musicbrowser.net;

import android.content.Context;
import android.util.Log;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by dalexiv on 7/18/16.
 */

public class DiskCache {
    private static final String TAG = DiskCache.class.getSimpleName();
    private static final String PERFORMERS_FILENAME = "performers.json";
    private static final Gson gson = new Gson();
    private final File file;
    private Type listType;

    public DiskCache(Context context) {
        listType = new TypeToken<ArrayList<Performer>>() {
        }.getType();
        file = new File(context.getCacheDir(), PERFORMERS_FILENAME);
    }

    public void saveToDisk(ArrayList<Performer> listForSave) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(gson.toJson(listForSave).getBytes());
            bos.flush();
            bos.close();
        } catch (IOException e) {
            Log.e(TAG, "Saving to disk failed", e);
        }
    }

    public ArrayList<Performer> restoreFromDisk(){
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(bytes, 0, bytes.length);
            bis.close();
        } catch (IOException e) {
            Log.e(TAG, "Loading from disk failed");
            e.printStackTrace();
            return null;
        }

        String json = new String(bytes);
        return gson.fromJson(json, listType);
    }

    public void clear() {
        file.delete();
    }
}
