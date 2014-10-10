package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.AnimalType;
import com.ambergleam.petfinder.model.SearchResponse;
import com.ambergleam.petfinder.model.Preference;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;

public class PetfinderService {

    private static final String TAG = PetfinderService.class.getSimpleName();

    private static final String ENDPOINT = "http://api.petfinder.com";
    private static final String API_KEY = "afc53e5040ea9a794a49b92de329d138";
    private static final String OUTPUT = "basic";
    private static final String FORMAT = "json";

    private final PetfinderServiceInterface mServiceInterface;
    private Preference mPreference;

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

    public Observable<SearchResponse> search() {
        if (mPreference.getAnimalType() == AnimalType.ALL) {
            return mServiceInterface.search(
                    API_KEY,
                    OUTPUT,
                    FORMAT
            );
        } else {
            return mServiceInterface.search(
                    API_KEY,
                    mPreference.getAnimalType().getString(),
                    OUTPUT,
                    FORMAT
            );
        }
    }

    public void setPreference(Preference preference) {
        mPreference = preference;
    }

}