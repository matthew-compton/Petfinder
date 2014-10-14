package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pet implements Serializable {

    @SerializedName("status")
    public Status mStatus;

    @SerializedName("description")
    public Description mDescription;

    @SerializedName("sex")
    public Gender mGender;

    @SerializedName("age")
    public Age mAge;

    @SerializedName("size")
    public Size mSize;

    @SerializedName("mix")
    public Mix mMix;

    @SerializedName("name")
    public Name mName;

    @SerializedName("animal")
    public Animal mAnimal;

    @SerializedName("media")
    public Media mMedia;

    @SerializedName("contact")
    public Contact mContact;

    public String toString() {
        return new StringBuilder()
                .append(mName.mString).append(", ")
                .append(mAnimal.mString)
                .toString();
    }

}