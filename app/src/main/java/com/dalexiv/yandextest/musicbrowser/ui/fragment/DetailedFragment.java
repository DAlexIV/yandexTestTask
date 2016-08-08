package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.presenters.DetailedStringPresenter;
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
    LinearLayout linearLayout;
    @BindView(R.id.imageView)
    ImageView imageArtist;
    @BindView(R.id.perfGenre)
    TextView textGenres;
    @BindView(R.id.perfStats)
    TextView textStats;
    @BindView(R.id.perfDesc)
    TextView textDescription;
    @BindView(R.id.perfLink)
    TextView textLink;
    private Unbinder unbinder;

    // Current performer
    private Performer performer;

    private DetailedStringPresenter presenter;

    public static DetailedFragment newInstance() {
        return new DetailedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO refactor into DI dependency
        // Initializing presenter with received performer
        presenter = new DetailedStringPresenter();
    }

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

        presenter.bindView(this, performer);

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
                .into(imageArtist);

        // Setting various emailText views
        textGenres.setText(presenter.generateGenres());
        textStats.setText(presenter.generateStats());
        textDescription.setText(presenter.generateDescription());

        // Optional link
        if (performer.getLink() == null) {
            textLink.setVisibility(View.GONE);
        } else {
            textLink.setText(presenter.generateLink());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        presenter.unbindView(this);
        unbinder.unbind();
    }
}
