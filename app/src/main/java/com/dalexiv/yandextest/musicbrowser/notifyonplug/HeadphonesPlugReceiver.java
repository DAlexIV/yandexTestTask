package com.dalexiv.yandextest.musicbrowser.notifyonplug;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.dalexiv.yandextest.musicbrowser.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by dalexiv on 7/20/16.
 */

public class HeadphonesPlugReceiver extends BroadcastReceiver {
    private static final String RECEIVER_TAG = HeadphonesPlugReceiver.class.getSimpleName();
    public static final String MUSIC_URL = "https://music.yandex.ru";
    public static final String RADIO_URL = "https://radio.yandex.ru";
    private boolean isPlugged = false;
    private static final int NOTIFICATION_ID = 1777;


    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", -1);
        if (state == 1) {
            if (!isPlugged) {
                showNotification(context);
            }
            isPlugged = true;
            Log.d(RECEIVER_TAG, "Headphones plugged");

        } else {
            if (isPlugged) {
                hideNotification(context);
            }
            isPlugged = false;
            Log.d(RECEIVER_TAG, "Headphones unplugged");
        }
    }

    public void showNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.notification_layout);
        remoteViews.setOnClickPendingIntent(R.id.music_link, setIntentToLink(context, MUSIC_URL));
        remoteViews.setOnClickPendingIntent(R.id.radio_link, setIntentToLink(context, RADIO_URL));

        Notification notification = builder.build();
        notification.contentView = remoteViews;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void hideNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent setIntentToLink(Context context, String link) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(link));
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}

