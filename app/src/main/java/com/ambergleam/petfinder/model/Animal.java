package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Animal implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return AnimalEnum.fromUrlFormatString(mString).toString();
    }

    public enum AnimalEnum {

        ALL, BARNYARD, BIRD, CAT, DOG, HORSE, PIG, REPTILE, SMALLFURRY;

        public String toUrlFormatString() {
            if (this == ALL) {
                return "";
            }
            return this.name().toLowerCase();
        }

        public static AnimalEnum fromUrlFormatString(String str) {
            for (AnimalEnum type : AnimalEnum.values()) {
                if (type.toUrlFormatString().equals(str.toLowerCase())) {
                    return type;
                } else if (str.equals("Scales, Fins & Other")) {
                    return REPTILE;
                } else if (str.equals("Small & Furry")) {
                    return SMALLFURRY;
                }
            }
            return ALL;
        }

        @Override
        public String toString() {
            switch (this) {
                case BARNYARD:
                    return "Barnyard";
                case BIRD:
                    return "Bird";
                case CAT:
                    return "Cat";
                case DOG:
                    return "Dog";
                case HORSE:
                    return "Horse";
                case PIG:
                    return "Pig";
                case REPTILE:
                    return "Reptile";
                case SMALLFURRY:
                    return "Small and Furry";
                case ALL:
                default:
                    return "Any";
            }
        }

    }

}
