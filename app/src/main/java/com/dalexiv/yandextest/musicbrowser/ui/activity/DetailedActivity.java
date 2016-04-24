package com.dalexiv.yandextest.musicbrowser.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {
    ImageView mImageView;
    Performer performer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        performer = getIntent().getParcelableExtra("performer");
        mImageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load(performer.getCover().getSmall())
                .into(mImageView);
    }

}
