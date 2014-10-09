package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

public class Pet {

    @SerializedName("id")
    public String mId;

    @SerializedName("name")
    public String mName;

    @SerializedName("animal")
    public String mAnimal;

    @SerializedName("imageUrl")
    public String mImageUrl;

}