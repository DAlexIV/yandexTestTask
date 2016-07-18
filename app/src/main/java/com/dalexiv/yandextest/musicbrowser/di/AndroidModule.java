package com.dalexiv.yandextest.musicbrowser.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dalexiv on 7/18/16.
 */
@Module
public class AndroidModule {
    private final Context context;

    public AndroidModule(final Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }
}
