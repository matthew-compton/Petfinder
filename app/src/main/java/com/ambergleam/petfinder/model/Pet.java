package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pet implements Serializable {

    @SerializedName("id")
    public Id mId;

    @SerializedName("shelterPetId")
    public ShelterPetId mShelterPetId;

    @SerializedName("shelterId")
    public ShelterId mShelterId;

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

//    @SerializedName("options")
//    public Options mOptions;

//    @SerializedName("breeds")
//    public Breeds mBreeds;

    @SerializedName("contact")
    public Contact mContact;

    @SerializedName("lastUpdate")
    public LastUpdate mLastUpdate;

    public String toString() {
        return new StringBuilder()
                .append(mName.mString).append(", ")
                .append(mId.mString).append(", ")
                .append(mAnimal.mString)
                .toString();
    }

}