package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Options implements Serializable {

    @SerializedName("option")
    public Option[] mOption;

}
