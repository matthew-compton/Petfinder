package com.ambergleam.petfinder;

import com.ambergleam.petfinder.controller.DetailsActivity;
import com.ambergleam.petfinder.controller.DetailsFragment;
import com.ambergleam.petfinder.controller.FavoritesActivity;
import com.ambergleam.petfinder.controller.FavoritesFragment;
import com.ambergleam.petfinder.controller.MainActivity;
import com.ambergleam.petfinder.controller.MainFragment;
import com.ambergleam.petfinder.controller.SettingsActivity;
import com.ambergleam.petfinder.controller.SettingsFragment;
import com.ambergleam.petfinder.service.PetfinderService;
import com.ambergleam.petfinder.service.PetfinderServiceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                MainFragment.class,
                DetailsActivity.class,
                DetailsFragment.class,
                SettingsActivity.class,
                SettingsFragment.class,
                FavoritesActivity.class,
                FavoritesFragment.class
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
        return new PetfinderServiceManager(PetfinderService.newInstance(), new PetfinderPreference());
    }

}
