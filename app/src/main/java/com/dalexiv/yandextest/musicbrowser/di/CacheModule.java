package com.dalexiv.yandextest.musicbrowser.di;

import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.Cache;

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
    Cache provideCache(Context context) {
        return new Cache(context);
    }
}
