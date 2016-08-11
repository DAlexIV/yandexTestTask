package com.dalexiv.yandextest.musicbrowser.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.ui.activity.IFragmentInteraction;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.DetailedFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by dalexiv on 4/21/16.
 */
/*
    Adapter for recyclerview in MainActivity
 */
public class PerformersAdapter extends RecyclerView.Adapter<PerformersAdapter.ViewHolder> {
    private List<Performer> dataset;
    private Fragment fragment;
    private IGenerateStats iGenerateStats;

    public PerformersAdapter(Fragment fragment, IGenerateStats iGenerateStats) {
        this.fragment = fragment;
        this.iGenerateStats = iGenerateStats;
        this.dataset = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        // Setting appropriate OnClickListener
        final ViewHolder viewHolder = new ViewHolder(item);
        viewHolder.itemView.setOnClickListener(click -> {
            if (viewHolder.getAdapterPosition() == NO_POSITION)
                return;

            final IFragmentInteraction parentActivity
                    = (IFragmentInteraction) fragment.getActivity();

            DetailedFragment detailedFragment = DetailedFragment.newInstance();
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putParcelable("performer", dataset.get(viewHolder.getAdapterPosition()));
            detailedFragment.setArguments(fragmentArgs);
            parentActivity.replaceMeWithFragment(detailedFragment);
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Just redirect call to presenter
        Performer performer = dataset.get(position);

        // Loading preview image
        Picasso.with(fragment.getActivity())
                .load(performer.getCover().getSmall())
//                .placeholder(R.drawable.placeholder)
                .into(holder.mImageView);

        // Setting various text fields
        holder.mTextViewName.setText(performer.getName());
        holder.mTextViewGenre.setText(performer.getGenres());
        holder.mTextViewStats.setText(iGenerateStats.generateStats(performer, ", "));
    }

    public void addPerformer(Performer performer) {
        dataset.add(performer);

        // Notify about added element
        notifyItemInserted(dataset.size() - 1);
    }

    public void clearPerformers() {
        // Just redirect call to presenter
        dataset.clear();

        // Notify about removed elements
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /*
        Performer's viewModel
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewName;
        public TextView mTextViewGenre;
        public TextView mTextViewStats;

        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.performerImage);
            mTextViewName = (TextView) v.findViewById(R.id.performerName);
            mTextViewGenre = (TextView) v.findViewById(R.id.performerGenre);
            mTextViewStats = (TextView) v.findViewById(R.id.performerStats);
        }

    }

}
