package com.dalexiv.yandextest.musicbrowser.ui;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.domain.PerformersController;

/**
 * Created by dalexiv on 4/21/16.
 */
/*
    Adapter for recyclerview in MainActivity
 */
public class PerformersAdapter extends RecyclerView.Adapter<PerformersAdapter.ViewHolder> {
    private PerformersController controller;

    public PerformersAdapter(Fragment fragment) {
        controller = new PerformersController(fragment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        // Setting appropriate OnClickListener
        final ViewHolder viewHolder = new ViewHolder(item);
        viewHolder.itemView.setOnClickListener(controller
                .bindClickListenerByIndex(viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Just redirect call to controller
        controller.fillWithData(holder, position);
    }

    public void addPerformer(Performer performer) {
        // Just redirect call to controller
        controller.addPerfromer(performer);

        // Notify about added element
        notifyItemInserted(controller.getDataset().size());
    }

    public void clearPerformers() {
        // Just redirect call to controller
        controller.clearPerformers();

        // Notify about removed elements
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return controller.getDataset().size();
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
