package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LastUpdate implements Serializable {

    @SerializedName("$t")
    public String mString;

    /*
     * Example date:
     * 2014-09-25T22:06:36Z
     */
    private String getFormattedDateString() {
        return mString.split("T")[0];
    }

    public String toString() {
        return getFormattedDateString();
    }

}