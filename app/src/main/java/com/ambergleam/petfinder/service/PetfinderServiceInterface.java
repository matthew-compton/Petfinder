package com.ambergleam.petfinder.service;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface PetfinderServiceInterface {

    @GET("/pet.getRandom")
    public Observable<SearchResponse> search(
            @Query("key") String key,
            @Query("output") String output,
            @Query("format") String format
    );

    @GET("/pet.getRandom")
    public Observable<SearchResponse> search(
            @Query("key") String key,
            @Query("animal") String animal,
            @Query("output") String output,
            @Query("format") String format
    );

}
