package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pets implements Serializable {

    @SerializedName("pet")
    public Pet[] mPets;

}
