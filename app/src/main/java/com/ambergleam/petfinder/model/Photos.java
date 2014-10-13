package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photos implements Serializable {

    @SerializedName("photo")
    public Photo[] mPhotos;

}
