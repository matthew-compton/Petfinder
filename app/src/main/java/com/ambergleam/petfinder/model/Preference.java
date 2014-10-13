package com.ambergleam.petfinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    private static final String PREF_ANIMAL_TYPE = "animal_type";

    private Animal.AnimalType mAnimalType;

    public Preference() {
        mAnimalType = Animal.AnimalType.ALL;
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String animalType = preferences.getString(PREF_ANIMAL_TYPE, Animal.AnimalType.ALL.getString());
        mAnimalType = Animal.AnimalType.fromString(animalType);
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ANIMAL_TYPE, mAnimalType.getString());
        editor.apply();
    }

    public Animal.AnimalType getAnimalType() {
        return mAnimalType;
    }

    public void setAnimalType(Animal.AnimalType animalType) {
        mAnimalType = animalType;
    }

}
