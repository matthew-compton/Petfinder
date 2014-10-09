package com.ambergleam.petfinder;

import android.app.Application;
import android.content.Context;

import com.crittercism.app.Crittercism;

import dagger.ObjectGraph;

public class PetfinderApplication extends Application {

    private static final String CRITTERCISM_APP_ID = "5436a312b573f14f10000006";

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        Crittercism.initialize(getApplicationContext(), CRITTERCISM_APP_ID);
        mObjectGraph = ObjectGraph.create(new PetfinderApplicationModule(this));
    }

    public static PetfinderApplication get(Context context) {
        return (PetfinderApplication) context.getApplicationContext();
    }

    public final void inject(Object object) {
        mObjectGraph.inject(object);
    }

}
