package com.dalexiv.yandextest.musicbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalexiv.yandextest.musicbrowser.contollers.PerformersController;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

/**
 * Created by dalexiv on 4/21/16.
 */
public class PerformersAdapter extends RecyclerView.Adapter<PerformersAdapter.ViewHolder> {
    PerformersController controller;

    public PerformersAdapter(Context context) {
        controller = new PerformersController(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        controller.fillWithData(holder, position);
    }

    public void addPerformer(Performer performer) {
        controller.addPerfromer(performer);
        notifyItemInserted(controller.getDataset().size());
    }

    public void clearPerformers() {
        controller.clearPerformers();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return controller.getDataset().size();
    }

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
