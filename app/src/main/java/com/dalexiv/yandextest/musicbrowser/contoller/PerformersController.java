package com.dalexiv.yandextest.musicbrowser.contoller;

import android.content.Context;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dalexiv on 4/24/16.
 */
public class PerformersController {
    private List<Performer> dataset;
    private Context context;

    // Constants
    private String[] albumsEnding;
    private String[] tracksEnding;

    public PerformersController(Context context) {
        this.dataset = new ArrayList<>();
        this.context = context;
        albumsEnding = context.getResources().getStringArray(R.array.albums);
        tracksEnding = context.getResources().getStringArray(R.array.tracks);
    }

    public void fillWithData(PerformersAdapter.ViewHolder holder, int position) {
        Performer perf = dataset.get(position);

        Picasso.with(context)
                .load(perf.getCover().getSmall())
                .into(holder.mImageView);
        holder.mTextViewName.setText(perf.getName());
        holder.mTextViewGenre.setText(Arrays.toString(perf.getGenres()).replaceAll("[\\[\\]]", ""));
        holder.mTextViewStats.setText(buildStats(perf.getAlbums(), perf.getTracks()));
    }

    private String buildStats(int albums, int tracks) {
        String constructedString = albums + " " + buildEnding(albums, albumsEnding);
        constructedString += ", " + tracks + " " + buildEnding(tracks, tracksEnding);
        return constructedString;
    }

    public static String buildEnding(int labels, String[] endings) {
        if (endings.length != 3)
            throw new IllegalArgumentException("Wrong number of ending passed to method");

        String ending;
        int scaledLabels = labels % 100;

        if (scaledLabels / 10 % 10 != 1 && scaledLabels % 10 == 1)
            ending = endings[0];
        else if (scaledLabels / 10 % 10 != 1
                && scaledLabels % 10 >= 2 && scaledLabels % 10 <= 4)
            ending = endings[1];
        else
            ending = endings[2];
        return ending;
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
