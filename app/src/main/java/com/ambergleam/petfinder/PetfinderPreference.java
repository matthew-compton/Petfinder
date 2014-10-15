package com.ambergleam.petfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ambergleam.petfinder.model.Animal;
import com.ambergleam.petfinder.model.Size;

public class PetfinderPreference {

    private static final String PREF_ANIMAL = "animal";
    private static final String PREF_SIZE = "size";

    private Animal.AnimalEnum mAnimalEnum;
    private Size.SizeEnum mSizeEnum;

    public PetfinderPreference() {
        mAnimalEnum = Animal.AnimalEnum.ALL;
        mSizeEnum = Size.SizeEnum.ANY;
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String animal = preferences.getString(PREF_ANIMAL, Animal.AnimalEnum.ALL.toUrlFormatString());
        mAnimalEnum = Animal.AnimalEnum.fromUrlFormatString(animal);

        String size = preferences.getString(PREF_SIZE, Size.SizeEnum.ANY.toUrlFormatString());
        mSizeEnum = Size.SizeEnum.fromUrlFormatString(size);
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ANIMAL, mAnimalEnum.toUrlFormatString());
        editor.putString(PREF_SIZE, mSizeEnum.toUrlFormatString());
        editor.apply();
    }

    public Animal.AnimalEnum getAnimalEnum() {
        return mAnimalEnum;
    }

    public void setAnimalEnum(Animal.AnimalEnum animalEnum) {
        mAnimalEnum = animalEnum;
    }

    public Size.SizeEnum getSizeEnum() {
        return mSizeEnum;
    }

    public void setSizeEnum(Size.SizeEnum sizeEnum) {
        mSizeEnum = sizeEnum;
    }

}
