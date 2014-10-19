package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.BuildConfig;
import com.ambergleam.petfinder.PetfinderPreference;
import com.ambergleam.petfinder.model.SearchResponse;
import com.ambergleam.petfinder.model.SearchResponseLocation;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;

public class PetfinderService {

    private static final String TAG = PetfinderService.class.getSimpleName();

    public static final String PETFINDER_API_KEY = BuildConfig.PETFINDER_API_KEY;

    public static final String ENDPOINT = "http://api.petfinder.com";
    private static final String FUNCTION_SEARCH_WITH_LOCATION = "find";
    private static final String FUNCTION_SEARCH = "getRandom";

    private static final String OUTPUT = "full";
    private static final String FORMAT = "json";
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

    public Observable<SearchResponseLocation> searchWithLocation(int offset) {
        return mServiceInterface.searchWithLocation(
                FUNCTION_SEARCH_WITH_LOCATION,
                PETFINDER_API_KEY,
                OUTPUT,
                FORMAT,
                COUNT,
                mPetfinderPreference.getAnimalEnum().toUrlFormatString(),
                mPetfinderPreference.getSizeEnum().toUrlFormatString(),
                mPetfinderPreference.getLocationUrlFormatString(),
                offset
        );
    }

    public Observable<SearchResponse> search() {
        return mServiceInterface.search(
                FUNCTION_SEARCH,
                PETFINDER_API_KEY,
                OUTPUT,
                FORMAT,
                COUNT,
                mPetfinderPreference.getAnimalEnum().toUrlFormatString(),
                mPetfinderPreference.getSizeEnum().toUrlFormatString()
        );
    }

}