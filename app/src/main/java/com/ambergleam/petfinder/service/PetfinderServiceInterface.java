package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.SearchResponse;
import com.ambergleam.petfinder.model.SearchResponseLocation;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface PetfinderServiceInterface {

    @GET("/pet.{function}")
    public Observable<SearchResponse> search(
            @Path("function") String function,
            @Query("key") String key,
            @Query("output") String output,
            @Query("format") String format,
            @Query("count") String count,
            @Query("animal") String animal,
            @Query("size") String size
    );

    @GET("/pet.{function}")
    public Observable<SearchResponse> searchById(
            @Path("function") String function,
            @Query("key") String key,
            @Query("format") String format,
            @Query("id") String id
    );

    @GET("/pet.{function}")
    public Observable<SearchResponseLocation> searchWithLocation(
            @Path("function") String function,
            @Query("key") String key,
            @Query("output") String output,
            @Query("format") String format,
            @Query("count") String count,
            @Query("animal") String animal,
            @Query("size") String size,
            @Query("location") String location,
            @Query("offset") int offset
    );

}
