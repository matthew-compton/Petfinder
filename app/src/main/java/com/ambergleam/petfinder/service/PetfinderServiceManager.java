package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.Petfinder;
import com.ambergleam.petfinder.model.Preference;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PetfinderServiceManager {

    private static final String TAG = PetfinderServiceManager.class.getSimpleName();

    private PetfinderService mPetfinderService;
    private Preference mPreference;

    public PetfinderServiceManager(PetfinderService petfinderService, Preference preference) {
        mPetfinderService = petfinderService;
        setPreference(preference);
    }

    public Observable<Petfinder> performSearch() {
        return mPetfinderService.search()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResponse -> Observable.from(searchResponse.mPetfinder));
    }

    public Preference getPreference() {
        return mPreference;
    }

    public void setPreference(Preference preference) {
        mPreference = preference;
        mPetfinderService.setPreference(preference);
    }

}