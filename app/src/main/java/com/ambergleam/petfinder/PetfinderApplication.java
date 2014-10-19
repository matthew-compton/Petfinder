package com.ambergleam.petfinder;

import android.app.Application;
import android.content.Context;

import com.crittercism.app.Crittercism;

import dagger.ObjectGraph;

public class PetfinderApplication extends Application {

    private static final String CRITTERCISM_APP_ID = BuildConfig.CRITTERCISM_APP_ID;

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.ENABLE_CRITTERCISM) {
            Crittercism.initialize(getApplicationContext(), CRITTERCISM_APP_ID);
        }
        mObjectGraph = ObjectGraph.create(new PetfinderApplicationModule(this));
    }

    public static PetfinderApplication get(Context context) {
        return (PetfinderApplication) context.getApplicationContext();
    }

    public final void inject(Object object) {
        mObjectGraph.inject(object);
    }

}
