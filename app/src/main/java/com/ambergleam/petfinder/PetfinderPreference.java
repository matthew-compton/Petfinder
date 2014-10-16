package com.ambergleam.petfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ambergleam.petfinder.model.Animal;
import com.ambergleam.petfinder.model.Size;

public class PetfinderPreference {

    private static final String PREF_ANIMAL = "animal";
    private static final String PREF_SIZE = "size";
    private static final String PREF_LOCATION = "location";

    private Animal.AnimalEnum mAnimalEnum;
    private Size.SizeEnum mSizeEnum;
    private String mLocationString;

    public PetfinderPreference() {
        mAnimalEnum = Animal.AnimalEnum.ALL;
        mSizeEnum = Size.SizeEnum.ANY;
        mLocationString = "";
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String animal = preferences.getString(PREF_ANIMAL, Animal.AnimalEnum.ALL.toUrlFormatString());
        mAnimalEnum = Animal.AnimalEnum.fromUrlFormatString(animal);

        String size = preferences.getString(PREF_SIZE, Size.SizeEnum.ANY.toUrlFormatString());
        mSizeEnum = Size.SizeEnum.fromUrlFormatString(size);

        mLocationString = preferences.getString(PREF_LOCATION, "");
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ANIMAL, mAnimalEnum.toUrlFormatString());
        editor.putString(PREF_SIZE, mSizeEnum.toUrlFormatString());
        editor.putString(PREF_LOCATION, mLocationString);
        editor.apply();
    }

    public boolean isLocationSearch() {
        if (mLocationString == null || mLocationString.equals("")) {
            return false;
        }
        return true;
    }

    public String getLocationString() {
        return mLocationString;
    }

    public void setLocationString(String locationString) {
        mLocationString = locationString;
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

    public boolean isDifferentFrom(PetfinderPreference pref) {
        if (mAnimalEnum != pref.getAnimalEnum() || mSizeEnum != pref.getSizeEnum() || !mLocationString.equals(pref.getLocationString())) {
            return true;
        }
        return false;
    }

}
