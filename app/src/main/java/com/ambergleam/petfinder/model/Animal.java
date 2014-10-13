package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Animal implements Serializable {

    @SerializedName("$t")
    public String mString;

    public enum AnimalType {

        BARNYARD, BIRD, CAT, DOG, HORSE, PIG, REPTILE, SMALLFURRY, ALL;

        public String getString() {
            return this.name().toLowerCase();
        }

        public static AnimalType fromString(String str) {
            for (AnimalType type : AnimalType.values()) {
                if (type.getString().equals(str.toLowerCase())) {
                    return type;
                }
            }
            return ALL;
        }

    }

}
