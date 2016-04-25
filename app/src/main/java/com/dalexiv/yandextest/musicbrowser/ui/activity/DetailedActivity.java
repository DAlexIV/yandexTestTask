package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.contollers.DetailedController;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.squareup.picasso.Picasso;
/*
    Activity with detailed data about selected performer
 */
public class DetailedActivity extends AppCompatActivity {
    // References to view
    ImageView mImageArtist;
    TextView mTextGenres;
    TextView mTextStats;
    TextView mTextDescription;
    TextView mTextLink;
    LinearLayout mLinearLayout;

    // Current performer
    Performer performer;

    DetailedController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // Initializing toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing views (Butterknife for wimps)
        mLinearLayout = (LinearLayout) findViewById(R.id.linearContainer);
        mImageArtist = (ImageView) findViewById(R.id.imageView);
        mTextGenres = (TextView) findViewById(R.id.perfGenre);
        mTextStats = (TextView) findViewById(R.id.perfStats);
        mTextDescription = (TextView) findViewById(R.id.perfDesc);
        mTextLink = (TextView) findViewById(R.id.perfLink);

        // Getting the performer from intent
        performer = getIntent().getParcelableExtra("performer");

        // If it's null, basically inform the user
        if (performer == null) {
            Toast.makeText(this, "No performer given", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initializing controller with received performer
        controller = new DetailedController(performer, this);

        fillAcvitiyWithData();

        // Configuring actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(performer.getName());
        }
    }

    private void fillAcvitiyWithData() {
        // Loading big image
        Picasso.with(this)
                .load(performer.getCover().getBig())
//                .placeholder(R.drawable.placeholder)
                .into(mImageArtist);

        // Setting various text views
        mTextGenres.setText(controller.generateGenres());
        mTextStats.setText(controller.generateStats());
        mTextDescription.setText(controller.generateDescription());

        // Optional link
        if (performer.getLink() == null) {
            mLinearLayout.removeView(mTextLink);
        } else {
            mTextLink.setText(controller.generateLink());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
