package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Breeds implements Serializable {

    @SerializedName("breed")
    public Breed mBreeds;

}
