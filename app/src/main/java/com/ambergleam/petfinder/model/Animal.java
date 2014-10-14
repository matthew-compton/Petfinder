package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Animal implements Serializable {

    @SerializedName("$t")
    public String mString;

    public enum AnimalEnum {

        BARNYARD, BIRD, CAT, DOG, HORSE, PIG, REPTILE, SMALLFURRY, ALL;

        public String toUrlFormatString() {
            return this.name().toLowerCase();
        }

        public static AnimalEnum fromUrlFormatString(String str) {
            for (AnimalEnum type : AnimalEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ALL;
        }

    }

}
