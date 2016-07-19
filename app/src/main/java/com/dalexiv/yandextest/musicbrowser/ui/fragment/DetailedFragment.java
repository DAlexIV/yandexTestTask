package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.domain.DetailedController;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
    Fragment with detailed data about selected performer
 */
public class DetailedFragment extends Fragment {
    // Layout
    @BindView(R.id.linearContainer)
    LinearLayout mLinearLayout;
    @BindView(R.id.imageView)
    ImageView mImageArtist;
    @BindView(R.id.perfGenre)
    TextView mTextGenres;
    @BindView(R.id.perfStats)
    TextView mTextStats;
    @BindView(R.id.perfDesc)
    TextView mTextDescription;
    @BindView(R.id.perfLink)
    TextView mTextLink;
    Unbinder unbinder;

    // Current performer
    Performer performer;

    DetailedController controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Getting the performer from intent
        performer = getArguments().getParcelable("performer");

        // If it's null, basically inform the user
        if (performer == null) {
            Toast.makeText(getActivity(), "No performer given", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO refactor into DI dependency
        // Initializing controller with received performer
        controller = new DetailedController(performer, this);

        fillAcvitiyWithData();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // Configuring actionbar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(performer.getName());
        }
    }

    private void fillAcvitiyWithData() {
        // Loading big image
        Picasso.with(getActivity())
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
