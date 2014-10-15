package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

public class Petfinder {

    @SerializedName("pets")
    public Pets mPets;

    @SerializedName("lastOffset")
    public LastOffset mLastOffset;

}
