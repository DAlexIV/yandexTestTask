package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.di.ActivityInjectors;
import com.dalexiv.yandextest.musicbrowser.net.DiskCache;
import com.dalexiv.yandextest.musicbrowser.notifyonplug.HeadphonesPlugReceiver;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.FragmentAbout;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.PerformersFragment;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.SendEmailFragment;

import javax.inject.Inject;

/*
    Activity with performers preview
 */
public class MainActivity extends AppCompatActivity implements IFragmentInteraction {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    DiskCache cache;
    private HeadphonesPlugReceiver receiver;

    private static final Uri CONTENT_URI
            = Uri.parse("content://com.yandex.perfstorage/main.sqlite/1");

    @Override
    protected void onResume() {
        registerReceiver(receiver,
                new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityInjectors.inject(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame_layout, PerformersFragment.newInstance(), "TAG")
                    .commit();
        }

        receiver = new HeadphonesPlugReceiver();
        getContentResolver().query(CONTENT_URI, null, null, null, null);
    }


    // Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
            case R.id.action_about:
                replaceMeWithFragment(FragmentAbout.newInstance());
                return true;
            case R.id.action_send_email:
                replaceMeWithFragment(SendEmailFragment.newInstance());
                return true;
            case R.id.action_invalidate_caches:
                cache.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void replaceMeWithFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, fragment)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
