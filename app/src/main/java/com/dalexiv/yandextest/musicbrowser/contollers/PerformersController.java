package com.dalexiv.yandextest.musicbrowser.contollers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.dalexiv.yandextest.musicbrowser.PerformersAdapter;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.ui.activity.DetailedActivity;
import com.squareup.picasso.Picasso;

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

    public PerformersController(Context context) {
        super(context);
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
        Picasso.with(context)
                .load(performer.getCover().getSmall())
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

        // If everything OK, then return click listener
        return v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("performer", dataset.get(index));

            // Adding various transition options
            ActivityOptionsCompat options
                    = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                    new Pair<>(holder.mImageView, "picture"),
                    new Pair<>(holder.mTextViewGenre, "genre"),
                    new Pair<>(holder.mTextViewStats, "stats"));
            context.startActivity(intent, options.toBundle());
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
