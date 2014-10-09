package com.ambergleam.petfinder.service;

import com.ambergleam.petfinder.model.Pet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("pets")
    public List<Pet> mPets;

    @Override
    public String toString() {
        return "SearchResponse{" +
                "mPets=" + mPets +
                '}';
    }

}
