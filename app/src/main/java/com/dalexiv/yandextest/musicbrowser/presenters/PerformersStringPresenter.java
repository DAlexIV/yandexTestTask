package com.dalexiv.yandextest.musicbrowser.presenters;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.ui.PerformersAdapter;
import com.dalexiv.yandextest.musicbrowser.ui.activity.IFragmentInteraction;
import com.dalexiv.yandextest.musicbrowser.ui.fragment.DetailedFragment;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dalexiv on 4/24/16.
 */
/*
    Encapsulates logic of generating and setting data at PerformersAdapter
 */
public class PerformersStringPresenter extends BaseStringPresenter {
    // Current list of performers in adapter
    private List<Performer> dataset;

    public PerformersStringPresenter(Fragment fragment) {
        super(fragment);
        this.dataset = new ArrayList<>();
    }


    /**
     * Fills performer's ViewHolder with data
     *
     * @param holder   performer's ViewHolder
     * @param position index of that ViewHolder
     */
    public void fillWithData(PerformersAdapter.ViewHolder holder, int position) {
        Performer performer = dataset.get(position);

        // Loading preview image
        Picasso.with(fragment.get().getActivity())
                .load(performer.getCover().getSmall())
//                .placeholder(R.drawable.placeholder)
                .into(holder.mImageView);

        // Setting various text fields
        holder.mTextViewName.setText(performer.getName());
        holder.mTextViewGenre.setText(Arrays.toString(performer.getGenres()).replaceAll("[\\[\\]]", ""));
        holder.mTextViewStats.setText(generateStats(performer, ", "));
    }

    /**
     * Binds click listener to view
     *
     * @param holder viewholder of bindable view
     * @return generated click listener
     */
    public View.OnClickListener bindClickListenerByIndex(PerformersAdapter.ViewHolder holder) {
        return v -> {
            final IFragmentInteraction parentActivity
                    = (IFragmentInteraction) fragment.get().getActivity();

            DetailedFragment detailedFragment = DetailedFragment.newInstance();
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putParcelable("performer", dataset.get(holder.getAdapterPosition()));
            detailedFragment.setArguments(fragmentArgs);
            parentActivity
                    .replaceMeWithFragment(detailedFragment);
        };
    }

    public void addPerfromer(Performer perf) {
        dataset.add(perf);
    }

    public void clearPerformers() {
        dataset.clear();
    }

    public List<Performer> getDataset() {
        return dataset;
    }

    public void setDataset(List<Performer> dataset) {
        this.dataset = dataset;
    }

    public Fragment getFragment() {
        return fragment.get();
    }

    public void setContext(Fragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }
}
