package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalexiv.yandextest.musicbrowser.R;

/**
 * Created by dalexiv on 7/20/16.
 */

public class FragmentAbout extends Fragment {
    public static FragmentAbout newInstance() {
        return new FragmentAbout();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}