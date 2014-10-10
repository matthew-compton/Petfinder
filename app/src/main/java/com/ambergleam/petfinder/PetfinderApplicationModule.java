package com.ambergleam.petfinder;

import com.ambergleam.petfinder.controller.MainActivity;
import com.ambergleam.petfinder.controller.MainFragment;
import com.ambergleam.petfinder.controller.SettingsActivity;
import com.ambergleam.petfinder.controller.SettingsFragment;
import com.ambergleam.petfinder.model.Preference;
import com.ambergleam.petfinder.service.PetfinderService;
import com.ambergleam.petfinder.service.PetfinderServiceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                MainFragment.class,
                SettingsActivity.class,
                SettingsFragment.class
        },
        complete = true)
public class PetfinderApplicationModule {

    private final PetfinderApplication mApplication;

    public PetfinderApplicationModule(PetfinderApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public PetfinderServiceManager providePetfinderServiceManager() {
        return new PetfinderServiceManager(PetfinderService.newInstance(), new Preference());
    }

}
