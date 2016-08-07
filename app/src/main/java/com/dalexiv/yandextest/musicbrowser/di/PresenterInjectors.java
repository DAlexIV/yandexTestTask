package com.dalexiv.yandextest.musicbrowser.di;

import com.dalexiv.yandextest.musicbrowser.MusicBrowserApplication;
import com.dalexiv.yandextest.musicbrowser.presenters.PerformersStringPresenter;

/**
 * Created by dalexiv on 7/17/16.
 */

public class PresenterInjectors {
    public static void inject(PerformersStringPresenter presenter) {
        getAppDiComponent(presenter).inject(presenter);
    }

    public static AppDiComponent getAppDiComponent(PerformersStringPresenter presenter) {
        return MusicBrowserApplication.from(presenter.getView().getActivity()).getComponent();
    }
}
