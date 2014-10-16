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
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder.mPets.mPets))
                .filter(pet -> pet.mMedia.mPhotos != null)
                .toList();
    }

    public Observable<List<Pet>> performSearch() {
        return mPetfinderService.search()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder.mPets))
                .filter(pet -> pet.mMedia.mPhotos != null)
                .toList();
    }

    public PetfinderPreference getPetfinderPreference() {
        return mPetfinderPreference;
    }

    public void setPetfinderPreference(PetfinderPreference petfinderPreference) {
        mPetfinderPreference = petfinderPreference;
        mPetfinderService.setPetfinderPreference(petfinderPreference);
    }

}