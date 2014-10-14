package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.SearchResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface PetfinderServiceInterface {

    @GET("/pet.getRandom")
    public Observable<SearchResponse> search(
            @Query("key") String key,
            @Query("output") String output,
            @Query("format") String format,
            @Query("animal") String animal,
            @Query("sex") String gender,
            @Query("size") String size
    );

}
