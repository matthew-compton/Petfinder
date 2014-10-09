package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.AnimalType;
import com.ambergleam.petfinder.model.Pet;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PetfinderServiceManager {

    private static final String TAG = PetfinderServiceManager.class.getSimpleName();

    private PetfinderService mPetfinderService;
    private AnimalType mAnimalTypePreference;

    public PetfinderServiceManager(PetfinderService petfinderService, AnimalType animalTypePreference) {
        mPetfinderService = petfinderService;
        mAnimalTypePreference = animalTypePreference;
        mPetfinderService.setAnimalTypePreference(mAnimalTypePreference);
    }

    public Observable<Pet> performSearch() {
        return mPetfinderService.search()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse.mPets)
                );
    }

}