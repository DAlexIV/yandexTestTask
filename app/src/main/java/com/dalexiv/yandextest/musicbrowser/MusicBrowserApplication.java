package com.dalexiv.yandextest.musicbrowser;

import android.app.Application;
import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.di.AndroidModule;
import com.dalexiv.yandextest.musicbrowser.di.AppDiComponent;
import com.dalexiv.yandextest.musicbrowser.di.DaggerAppDiComponent;
import com.facebook.stetho.Stetho;

/**
 * Created by dalexiv on 7/17/16.
 */

public class MusicBrowserApplication extends Application {
    private AppDiComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        component = DaggerAppDiComponent
                .builder()
                .androidModule(new AndroidModule(this))
                .build();
    }

    public AppDiComponent getComponent() {
        return component;
    }

    public static MusicBrowserApplication from(Context context) {
        return (MusicBrowserApplication) context.getApplicationContext();
    }
}
