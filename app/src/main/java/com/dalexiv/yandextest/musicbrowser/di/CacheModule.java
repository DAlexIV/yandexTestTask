package com.dalexiv.yandextest.musicbrowser.di;

import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.comms.DiskCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dalexiv on 7/18/16.
 */

@Module(includes = {AndroidModule.class})
public class CacheModule {
    @Singleton
    @Provides
    DiskCache provideCache(Context context) {
        return new DiskCache(context);
    }
}
