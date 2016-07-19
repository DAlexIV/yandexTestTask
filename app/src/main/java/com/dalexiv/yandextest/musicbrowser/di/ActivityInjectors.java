package com.dalexiv.yandextest.musicbrowser.di;

import com.dalexiv.yandextest.musicbrowser.MusicBrowserApplication;
import com.dalexiv.yandextest.musicbrowser.ui.activity.MainActivity;

/**
 * Created by dalexiv on 7/18/16.
 */

public class ActivityInjectors {
    public static void inject(MainActivity activity) {
        getAppDiComponent(activity).inject(activity);
    }
    public static AppDiComponent getAppDiComponent(MainActivity activity) {
        return MusicBrowserApplication.from(activity).getComponent();
    }
}
