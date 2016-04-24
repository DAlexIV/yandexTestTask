package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.contollers.DetailedController;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {
    ImageView mImageArtist;
    TextView mTextGenres;
    TextView mTextStats;
    TextView mTextDescription;
    Performer performer;

    DetailedController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageArtist = (ImageView) findViewById(R.id.imageView);
        mTextGenres = (TextView) findViewById(R.id.perfGenre);
        mTextStats = (TextView) findViewById(R.id.perfStats);
        mTextDescription = (TextView) findViewById(R.id.perfDesc);

        performer = getIntent().getParcelableExtra("performer");

        if (performer == null) {
            Toast.makeText(this, "No performer given", Toast.LENGTH_SHORT).show();
            return;
        }

        controller = new DetailedController(performer, this);

        Picasso.with(this)
                .load(performer.getCover().getBig())
                .into(mImageArtist);
        mTextGenres.setText(controller.generateGenres());
        mTextStats.setText(controller.generateStats());
        mTextDescription.setText(controller.generateDescription());


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(performer.getName());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
