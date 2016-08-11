package com.yandex.perfstorage;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by dalexiv on 8/11/16.
 */

public class PerfStorageApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }


}
