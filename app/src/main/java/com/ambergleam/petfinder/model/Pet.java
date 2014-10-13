package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pet implements Serializable {

    @SerializedName("id")
    public Id mId;

    @SerializedName("name")
    public Name mName;

    @SerializedName("animal")
    public Animal mAnimal;

    @SerializedName("media")
    public Media mMedia;

}