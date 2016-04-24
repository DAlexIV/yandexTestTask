package com.dalexiv.yandextest.musicbrowser.retro;

import com.dalexiv.yandextest.musicbrowser.dataModel.Performer;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by dalexiv on 4/22/16.
 */
/*
    Retrofit2 interface for downloading performers
 */
public interface IPerformer {
    @GET("artists.json")
    @Headers({"Content-Type: application/json"})
    Observable<ArrayList<Performer>> getPerformers();
}
