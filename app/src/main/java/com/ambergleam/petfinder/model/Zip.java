package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Zip implements Serializable {

    public static String DEFAULT = "";

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return mString;
    }

}