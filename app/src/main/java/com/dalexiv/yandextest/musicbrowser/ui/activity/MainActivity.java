package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.di.ActivityInjectors;
import com.dalexiv.yandextest.musicbrowser.net.DiskCache;
import com.dalexiv.yandextest.musicbrowser.notifyOnPlug.HeadphonesPlugReceiver;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.FragmentAbout;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.PerformersFragment;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.SendEmailFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

/*
    Activity with performers preview
 */
public class MainActivity extends RxAppCompatActivity implements IFragmentInteraction {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    DiskCache cache;
    private HeadphonesPlugReceiver receiver;

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

        if (savedInstanceState == null)
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_layout, PerformersFragment.newInstance())
                    .commit();

        receiver = new HeadphonesPlugReceiver();
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
                onBackPressed();
                return true;
            case R.id.action_about:
                replaceMeWithFragment(FragmentAbout.newInstance());
                return true;
            case R.id.action_send_email:
                replaceMeWithFragment(SendEmailFragment.newInstance());
                return true;
            case R.id.action_invalidate_caches:
                cache.flush();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void replaceMeWithFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
