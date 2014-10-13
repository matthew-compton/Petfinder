package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable {

    @SerializedName("$t")
    public String mPhotoUrl;

}