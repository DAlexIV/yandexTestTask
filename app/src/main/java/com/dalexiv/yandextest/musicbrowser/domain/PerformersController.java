package com.dalexiv.yandextest.musicbrowser.domain;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.ui.activity.IFragmentInteraction;
import com.dalexiv.yandextest.musicbrowser.ui.PerformersAdapter;
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
public class PerformersController extends BaseController {
    // Current list of performers in adapter
    private List<Performer> dataset;

    public PerformersController(Fragment fragment) {
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
        holder.mTextViewStats.setText(EndingBuilder.buildStats(performer.getAlbums(),
                performer.getTracks(), ", ", albumsEnding, tracksEnding));

        // Setting appropriate OnClickListener
        holder.itemView.setOnClickListener(bindClickListenerByIndex(position, holder));
    }

    /**
     * Binds click listener to view
     *
     * @param index index of bindable view
     * @return generated click listener
     */
    public View.OnClickListener bindClickListenerByIndex(int index, PerformersAdapter.ViewHolder holder) {
        // Handling corner cases
        if (index < 0 && index >= dataset.size())
            throw new IllegalArgumentException("Index is out of bound in adapter within RecyclerView");

        // If everything is OK, then return click listener
        return v -> {
            DetailedFragment detailedFragment = new DetailedFragment();
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putParcelable("performer", dataset.get(index));
            detailedFragment.setArguments(fragmentArgs);
            ((IFragmentInteraction) fragment.get().getActivity())
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
