package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.AnimalType;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;

public class PetfinderService {

    private static final String TAG = PetfinderService.class.getSimpleName();

    private static final String ENDPOINT = "http://api.petfinder.com";
    private static final String API_KEY = "afc53e5040ea9a794a49b92de329d138";
    private static final String OUTPUT = "full";
    private static final String FORMAT = "json";

    private final PetfinderServiceInterface mServiceInterface;
    private AnimalType mAnimalTypePreference;

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
        if (mAnimalTypePreference == AnimalType.ALL) {
            return mServiceInterface.search(
                    API_KEY,
                    OUTPUT,
                    FORMAT
            );
        } else {
            return mServiceInterface.search(
                    API_KEY,
                    mAnimalTypePreference.getString(),
                    OUTPUT,
                    FORMAT
            );
        }
    }

    public void setAnimalTypePreference(AnimalType animalTypePreference) {
        mAnimalTypePreference = animalTypePreference;
    }

}