package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.PetfinderPreference;
import com.ambergleam.petfinder.model.SearchResponse;
import com.ambergleam.petfinder.model.SearchResponseLocation;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;

public class PetfinderService {

    private static final String TAG = PetfinderService.class.getSimpleName();

    public static final String ENDPOINT = "http://api.petfinder.com";
    public static final String API_KEY = "afc53e5040ea9a794a49b92de329d138";
    public static final String OUTPUT = "full";
    public static final String FORMAT = "json";
    public static final String COUNT = "10";

    private final PetfinderServiceInterface mServiceInterface;
    private PetfinderPreference mPetfinderPreference;

    public static PetfinderService newInstance() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient())
                .build();
        return new PetfinderService(restAdapter.create(PetfinderServiceInterface.class));
    }

    private PetfinderService(PetfinderServiceInterface serviceInterface) {
        mServiceInterface = serviceInterface;
    }

    public void setPetfinderPreference(PetfinderPreference petfinderPreference) {
        mPetfinderPreference = petfinderPreference;
    }

    public Observable<SearchResponse> searchWithLocation(int offset) {
        return mServiceInterface.searchWithLocation(
                API_KEY,
                OUTPUT,
                FORMAT,
                COUNT,
                mPetfinderPreference.getAnimalEnum().toUrlFormatString(),
                mPetfinderPreference.getSizeEnum().toUrlFormatString(),
                mPetfinderPreference.getLocationUrlFormatString(),
                offset
        );
    }

    public Observable<SearchResponseLocation> search() {
        return mServiceInterface.search(
                API_KEY,
                OUTPUT,
                FORMAT,
                COUNT,
                mPetfinderPreference.getAnimalEnum().toUrlFormatString(),
                mPetfinderPreference.getSizeEnum().toUrlFormatString()
        );
    }

}