package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalexiv.yandextest.musicbrowser.R;
import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;
import com.dalexiv.yandextest.musicbrowser.presenters.PerformersStringPresenter;
import com.dalexiv.yandextest.musicbrowser.ui.DividerItemDecoration;
import com.dalexiv.yandextest.musicbrowser.ui.PerformersAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import rx.Observable;

/**
 * Created by dalexiv on 7/18/16.
 */

public class PerformersFragment extends Fragment implements IDisplayPerformers{
    // Constants
    private static final long ANIMATION_DURATION = 750;
    private static final int NUMBER_OF_ANIMATED_ITEMS = 5;

    // Layout
    @BindView(R.id.recyclerPerfs)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;

    private PerformersAdapter adapter;

    // Custom observable for better animation timings
    private Observable<Long> customInterval;
    private PerformersStringPresenter presenter;

    public static PerformersFragment newInstance() {
        return new PerformersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TO-DO inject with dagger
        presenter = new PerformersStringPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_performers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAnimationObservable();
        unbinder = ButterKnife.bind(this, view);
        presenter.bindView(this);

        // Refactor into interface
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Исполнители");
        }

        configureRecyclerViewAndAdapter();
        setupSwipeToRefresh();

    }

    private void setupSwipeToRefresh() {
        // Clearing old data and loading new one from network
        swipeRefreshLayout.setOnRefreshListener(() -> {
           presenter.doSwipeToRefresh();
        });

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                Color.YELLOW, ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    private void configureRecyclerViewAndAdapter() {
        // Initializing adapter
        adapter = new PerformersAdapter(this, (performer, splitter)
                -> presenter.generateStats(performer, splitter));

        // Configuring recyclerview
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
    }

    private void initAnimationObservable() {
        // Calculating animation values
        long ITEM_ANIMATION_DURATION = ANIMATION_DURATION / NUMBER_OF_ANIMATED_ITEMS;

        customInterval = Observable.interval(ITEM_ANIMATION_DURATION, TimeUnit.MILLISECONDS)
                .flatMap(time -> {
            if (time < NUMBER_OF_ANIMATED_ITEMS) // Take first with animation interval
                return Observable.just(time);
            else if (time == NUMBER_OF_ANIMATED_ITEMS) // Then emit others almost as one
                return Observable.interval(1, TimeUnit.MILLISECONDS);
            else // Finally, just return nothing
                return Observable.empty();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView(this);
        unbinder.unbind();
        adapter = null;
    }

    @Override
    public Observable<Long> getAnimationIntervalObservable() {
        return customInterval;
    }
    @Override
    public void setRefreshing(boolean isRefreshing) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.post(() -> {
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(isRefreshing);
            });
    }

    @Override
    public void notifyUser(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void addPerformer(Performer performer) {
        adapter.addPerformer(performer);
    }

    @Override
    public void clearPerformers() {
        adapter.clearPerformers();
    }
}
