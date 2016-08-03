package com.dalexiv.yandextest.musicbrowser.di;


import com.dalexiv.yandextest.musicbrowser.ui.activity.MainActivity;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.PerformersFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dalexiv on 7/17/16.
 */

@Singleton
@Component(modules = {NetModule.class, CacheModule.class})
public interface AppDiComponent {
    void inject(PerformersFragment fragment);

    void inject(MainActivity mainActivity);
}
