package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.PetfinderPreference;
import com.ambergleam.petfinder.model.Pet;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PetfinderServiceManager {

    private static final String TAG = PetfinderServiceManager.class.getSimpleName();

    private PetfinderService mPetfinderService;
    private PetfinderPreference mPetfinderPreference;

    public PetfinderServiceManager(PetfinderService petfinderService, PetfinderPreference petfinderPreference) {
        mPetfinderService = petfinderService;
        setPetfinderPreference(petfinderPreference);
    }

    public Observable<List<Pet>> performSearchWithLocation(int offset) {
        return mPetfinderService.searchWithLocation(offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse))
                .filter(searchResponse -> searchResponse != null && searchResponse.mPetfinder != null && searchResponse.mPetfinder.mPets != null && searchResponse.mPetfinder.mPets.mPets != null)
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder.mPets.mPets))
                .toList();
    }

    public Observable<List<Pet>> performSearch() {
        return mPetfinderService.search()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse))
                .filter(searchResponse -> searchResponse != null && searchResponse.mPetfinder != null && searchResponse.mPetfinder.mPets != null)
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder.mPets))
                .toList();
    }

    public Observable<List<Pet>> performSearchById(String id) {
        return mPetfinderService.searchById(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse))
                .filter(searchResponse -> searchResponse != null && searchResponse.mPetfinder != null && searchResponse.mPetfinder.mPets != null)
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder.mPets))
                .toList();
    }

    public int getCount() {
        return Integer.valueOf(mPetfinderService.COUNT);
    }

    public PetfinderPreference getPetfinderPreference() {
        return mPetfinderPreference;
    }

    public void setPetfinderPreference(PetfinderPreference petfinderPreference) {
        mPetfinderPreference = petfinderPreference;
        mPetfinderService.setPetfinderPreference(petfinderPreference);
    }

}