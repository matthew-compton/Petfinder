package com.ambergleam.petfinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    private static final String PREF_ANIMAL_TYPE = "animal_type";

    private Animal.AnimalEnum mAnimalEnum;

    public Preference() {
        mAnimalEnum = Animal.AnimalEnum.ALL;
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String animalType = preferences.getString(PREF_ANIMAL_TYPE, Animal.AnimalEnum.ALL.toUrlFormatString());
        mAnimalEnum = Animal.AnimalEnum.fromUrlFormatString(animalType);
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ANIMAL_TYPE, mAnimalEnum.toUrlFormatString());
        editor.apply();
    }

    public Animal.AnimalEnum getAnimalEnum() {
        return mAnimalEnum;
    }

    public void setAnimalEnum(Animal.AnimalEnum animalEnum) {
        mAnimalEnum = animalEnum;
    }

}
