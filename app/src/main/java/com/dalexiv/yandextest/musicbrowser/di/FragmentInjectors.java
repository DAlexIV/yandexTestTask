package com.dalexiv.yandextest.musicbrowser.di;

import android.app.Fragment;

import com.dalexiv.yandextest.musicbrowser.MusicBrowserApplication;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.PerformersFragment;

/**
 * Created by dalexiv on 7/17/16.
 */

public class FragmentInjectors {
    public static void inject(PerformersFragment fragment) {
        getAppDiComponent(fragment).inject(fragment);
    }

    public static AppDiComponent getAppDiComponent(Fragment fragment) {
        return MusicBrowserApplication.from(fragment.getActivity()).getComponent();
    }
}
