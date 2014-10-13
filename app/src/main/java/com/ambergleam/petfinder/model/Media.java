package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Media implements Serializable {

    @SerializedName("photos")
    public Photos mPhotos;

}