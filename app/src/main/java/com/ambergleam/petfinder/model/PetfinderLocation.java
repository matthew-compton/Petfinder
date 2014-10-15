package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

public class PetfinderLocation {

    @SerializedName("pet")
    public Pet[] mPets;

}
