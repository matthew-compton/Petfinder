package com.ambergleam.petfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ambergleam.petfinder.model.Animal;
import com.ambergleam.petfinder.model.Location;
import com.ambergleam.petfinder.model.Size;
import com.ambergleam.petfinder.model.State;
import com.ambergleam.petfinder.model.Zip;

import java.util.HashSet;
import java.util.Set;

public class PetfinderPreference {

    private static final String TAG = PetfinderPreference.class.getSimpleName();

    private static final String PREF_FAVORITES = "favorites";
    private static final String PREF_ANIMAL = "animal";
    private static final String PREF_SIZE = "size";
    private static final String PREF_LOCATION = "location";
    private static final String PREF_STATE = "state";
    private static final String PREF_ZIP = "zip";

    private Set<String> mFavorites;
    private Animal.AnimalEnum mAnimalEnum;
    private Size.SizeEnum mSizeEnum;
    private Location.LocationEnum mLocationEnum;
    private State.StateEnum mStateEnum;
    private String mZipString;

    public PetfinderPreference() {
        mFavorites = new HashSet<>();
        mAnimalEnum = Animal.AnimalEnum.ALL;
        mSizeEnum = Size.SizeEnum.ANY;
        mLocationEnum = Location.LocationEnum.ANY;
        mStateEnum = State.StateEnum.ANY;
        mZipString = Zip.DEFAULT;
    }

    public void loadPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        mFavorites = preferences.getStringSet(PREF_FAVORITES, new HashSet<>());
        Log.i(TAG, mFavorites.toString());

        String animal = preferences.getString(PREF_ANIMAL, Animal.AnimalEnum.ALL.toUrlFormatString());
        mAnimalEnum = Animal.AnimalEnum.fromUrlFormatString(animal);
        Log.i(TAG, mAnimalEnum.toString());

        String size = preferences.getString(PREF_SIZE, Size.SizeEnum.ANY.toUrlFormatString());
        mSizeEnum = Size.SizeEnum.fromUrlFormatString(size);
        Log.i(TAG, mSizeEnum.toString());

        String location = preferences.getString(PREF_LOCATION, Location.LocationEnum.ANY.toUrlFormatString());
        mLocationEnum = Location.LocationEnum.fromUrlFormatString(location);
        Log.i(TAG, mLocationEnum.toString());

        String state = preferences.getString(PREF_STATE, State.StateEnum.ANY.toUrlFormatString());
        mStateEnum = State.StateEnum.fromUrlFormatString(state);
        Log.i(TAG, mStateEnum.toString());

        mZipString = preferences.getString(PREF_ZIP, Zip.DEFAULT);
        Log.i(TAG, mZipString.toString());
    }

    public void savePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(PREF_FAVORITES, mFavorites);
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
                if (mStateEnum != State.StateEnum.ANY) {
                    return true;
                }
                return false;
            case ZIP:
                if (!mZipString.equals(Zip.DEFAULT)) {
                    return true;
                }
                return false;
            case ANY:
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
            case ANY:
            default:
                return "";
        }
    }

    public Set<String> getFavorites() {
        return mFavorites;
    }

    public void setFavorites(Set<String> favorites) {
        mFavorites = favorites;
    }

    public void addFavorite(String favorite) {
        mFavorites.add(favorite);
    }

    public void removeFavorite(String favorite) {
        mFavorites.remove(favorite);
    }

    public boolean isFavorite(String favorite) {
        return mFavorites.contains(favorite);
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
