package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Phone implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return mString;
    }

}