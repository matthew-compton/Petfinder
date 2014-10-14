package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.Petfinder;
import com.ambergleam.petfinder.PetfinderPreference;

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

    public Observable<Petfinder> performSearch() {
        return mPetfinderService.search()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder));
    }

    public PetfinderPreference getPetfinderPreference() {
        return mPetfinderPreference;
    }

    public void setPetfinderPreference(PetfinderPreference petfinderPreference) {
        mPetfinderPreference = petfinderPreference;
        mPetfinderService.setPetfinderPreference(petfinderPreference);
    }

}