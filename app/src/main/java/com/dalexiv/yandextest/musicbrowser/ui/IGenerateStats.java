package com.dalexiv.yandextest.musicbrowser.ui;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

/**
 * Created by dalexiv on 8/8/16.
 */
public interface IGenerateStats {
    String generateStats(Performer performer, String splitter);
}
