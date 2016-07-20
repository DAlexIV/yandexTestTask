package com.dalexiv.yandextest.musicbrowser.notifyOnPlug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dalexiv on 7/20/16.
 */

public class HeadphonesPlugReceiver extends BroadcastReceiver {
    private static final String RECEIVER_TAG = HeadphonesPlugReceiver.class.getSimpleName();
    private boolean isPlugged = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", -1);
        if (state == 1) {
            if (!isPlugged) {
                showNotification();
            }
            isPlugged = true;
            Log.d(RECEIVER_TAG, "Headphones plugged");

        } else {
            if (isPlugged) {
                hideNotification();
            }
            isPlugged = false;
            Log.d(RECEIVER_TAG, "Headphones unplugged");
        }
    }

    public void showNotification() {

    }

    public void hideNotification() {

    }
}

