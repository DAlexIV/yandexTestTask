package com.dalexiv.yandextest.musicbrowser.util;

/**
 * Created by dalexiv on 4/25/16.
 */
public class HelperMethods {
    public static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {

        }
    }
}
