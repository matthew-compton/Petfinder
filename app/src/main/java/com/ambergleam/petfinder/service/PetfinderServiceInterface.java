package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.SearchResponse;
import com.ambergleam.petfinder.model.SearchResponseLocation;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface PetfinderServiceInterface {

    @GET("/pet.find")
    public Observable<SearchResponseLocation> searchWithLocation(
            @Query("key") String key,
            @Query("output") String output,
            @Query("format") String format,
            @Query("count") String count,
            @Query("animal") String animal,
            @Query("size") String size,
            @Query("location") String location,
            @Query("offset") int offset
    );

    @GET("/pet.getRandom")
    public Observable<SearchResponse> search(
            @Query("key") String key,
            @Query("output") String output,
            @Query("format") String format,
            @Query("count") String count,
            @Query("animal") String animal,
            @Query("size") String size
    );

}
