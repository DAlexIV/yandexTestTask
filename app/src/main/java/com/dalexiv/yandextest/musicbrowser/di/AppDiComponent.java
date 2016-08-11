package com.dalexiv.yandextest.musicbrowser.di;


import com.dalexiv.yandextest.musicbrowser.presenters.PerformersStringPresenter;
import com.dalexiv.yandextest.musicbrowser.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dalexiv on 7/17/16.
 */

@Singleton
@Component(modules = {AndroidModule.class,
        CacheModule.class})
public interface AppDiComponent {
    void inject(PerformersStringPresenter presenter);

    void inject(MainActivity mainActivity);
}
