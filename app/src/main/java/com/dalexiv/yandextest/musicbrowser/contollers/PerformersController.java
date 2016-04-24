package com.dalexiv.yandextest.musicbrowser.contollers;

import android.content.Context;
import android.content.Intent;
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
    Encapsulates logic of generating and setting data at MainActivity
 */
public class PerformersController extends BaseController{
    private List<Performer> dataset;

    public PerformersController(Context context) {
        super(context);
        this.dataset = new ArrayList<>();
    }

    public void fillWithData(PerformersAdapter.ViewHolder holder, int position) {
        Performer performer = dataset.get(position);

        Picasso.with(context)
                .load(performer.getCover().getSmall())
                .into(holder.mImageView);
        holder.mTextViewName.setText(performer.getName());
        holder.mTextViewGenre.setText(Arrays.toString(performer.getGenres()).replaceAll("[\\[\\]]", ""));
        holder.mTextViewStats.setText(EndingBuilder.buildStats(performer.getAlbums(),
                performer.getTracks(), ", ", albumsEnding, tracksEnding));

        holder.itemView.setOnClickListener(bindClickListenerByIndex(position));
    }

    public View.OnClickListener bindClickListenerByIndex(int index) {
        if (index < 0 && index >= dataset.size())
            throw new IllegalArgumentException("Index is out of bound in adapter within RecyclerView");
        return v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("performer", dataset.get(index));
            context.startActivity(intent);
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
