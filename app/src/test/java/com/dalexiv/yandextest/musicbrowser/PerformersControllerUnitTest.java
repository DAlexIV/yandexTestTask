package com.dalexiv.yandextest.musicbrowser;

import com.dalexiv.yandextest.musicbrowser.contoller.PerformersController;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PerformersControllerUnitTest {
    @Test
    public void endingsTest1() throws Exception {
        for (int i = 0; i < 100; ++i)
            System.out.println(i + " " + PerformersController.buildEnding(i,
                    new String[] {"альбом", "альбома", "альбомов"}));
    }

    @Test
    public void endingsTest2() throws Exception {
        for (int i = 0; i < 100; ++i)
            System.out.println(i + " " + PerformersController.buildEnding(i,
                    new String[] {"песня", "песни", "песен"}));
    }
}