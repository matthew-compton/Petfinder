package com.ambergleam.petfinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    private static final String PREF_ANIMAL_TYPE = "animal_type";

    private AnimalType mAnimalType;

    public Preference() {
        mAnimalType = AnimalType.ALL;
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String animalType = preferences.getString(PREF_ANIMAL_TYPE, AnimalType.ALL.getString());
        mAnimalType = AnimalType.fromString(animalType);
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ANIMAL_TYPE, mAnimalType.getString());
        editor.apply();
    }

    public AnimalType getAnimalType() {
        return mAnimalType;
    }

    public void setAnimalType(AnimalType animalType) {
        mAnimalType = animalType;
    }

}
