package com.ambergleam.petfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ambergleam.petfinder.model.Animal;
import com.ambergleam.petfinder.model.Location;
import com.ambergleam.petfinder.model.Size;
import com.ambergleam.petfinder.model.State;
import com.ambergleam.petfinder.model.Zip;

public class PetfinderPreference {

    private static final String PREF_ANIMAL = "animal";
    private static final String PREF_SIZE = "size";
    private static final String PREF_LOCATION = "location";
    private static final String PREF_STATE = "state";
    private static final String PREF_ZIP = "zip";

    private Animal.AnimalEnum mAnimalEnum;
    private Size.SizeEnum mSizeEnum;
    private Location.LocationEnum mLocationEnum;
    private State.StateEnum mStateEnum;
    private String mZipString;

    public PetfinderPreference() {
        mAnimalEnum = Animal.AnimalEnum.ALL;
        mSizeEnum = Size.SizeEnum.ANY;
        mLocationEnum = Location.LocationEnum.UNSPECIFIED;
        mStateEnum = State.StateEnum.UNSPECIFIED;
        mZipString = Zip.DEFAULT;
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String animal = preferences.getString(PREF_ANIMAL, Animal.AnimalEnum.ALL.toUrlFormatString());
        mAnimalEnum = Animal.AnimalEnum.fromUrlFormatString(animal);

        String size = preferences.getString(PREF_SIZE, Size.SizeEnum.ANY.toUrlFormatString());
        mSizeEnum = Size.SizeEnum.fromUrlFormatString(size);

        String location = preferences.getString(PREF_LOCATION, Location.LocationEnum.UNSPECIFIED.toUrlFormatString());
        mLocationEnum = Location.LocationEnum.fromUrlFormatString(location);

        String state = preferences.getString(PREF_STATE, State.StateEnum.UNSPECIFIED.toUrlFormatString());
        mStateEnum = State.StateEnum.fromUrlFormatString(state);

        mZipString = preferences.getString(PREF_ZIP, Zip.DEFAULT);
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ANIMAL, mAnimalEnum.toUrlFormatString());
        editor.putString(PREF_SIZE, mSizeEnum.toUrlFormatString());
        editor.putString(PREF_LOCATION, mLocationEnum.toUrlFormatString());
        editor.putString(PREF_STATE, mStateEnum.toUrlFormatString());
        editor.putString(PREF_ZIP, mZipString);
        editor.apply();
    }

    public boolean isLocationSearch() {
        switch (mLocationEnum) {
            case STATE:
                if (mStateEnum != State.StateEnum.UNSPECIFIED) {
                    return true;
                }
                return false;
            case ZIP:
                if (!mZipString.equals(Zip.DEFAULT)) {
                    return true;
                }
                return false;
            case UNSPECIFIED:
            default:
                return false;
        }
    }

    public String getLocationUrlFormatString() {
        switch (mLocationEnum) {
            case STATE:
                return mStateEnum.toUrlFormatString();
            case ZIP:
                return mZipString;
            case UNSPECIFIED:
            default:
                return "";
        }
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

    public Location.LocationEnum getLocationEnum() {
        return mLocationEnum;
    }

    public void setLocationEnum(Location.LocationEnum locationEnum) {
        mLocationEnum = locationEnum;
    }

    public String getZipString() {
        return mZipString;
    }

    public void setZipString(String zipString) {
        mZipString = zipString;
    }

    public State.StateEnum getStateEnum() {
        return mStateEnum;
    }

    public void setStateEnum(State.StateEnum stateEnum) {
        mStateEnum = stateEnum;
    }

    public boolean isDifferentFrom(PetfinderPreference pref) {
        if (mAnimalEnum != pref.getAnimalEnum()
                || mSizeEnum != pref.getSizeEnum()
                || mLocationEnum != pref.getLocationEnum()
                || mStateEnum != pref.getStateEnum()
                || !mZipString.equals(pref.getZipString())
                ) {
            return true;
        }
        return false;
    }

}
