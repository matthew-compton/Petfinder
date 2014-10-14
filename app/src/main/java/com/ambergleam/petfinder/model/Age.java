package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Age implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return AgeEnum.fromUrlFormatString(mString).toString();
    }

    public enum AgeEnum {

        ANY, BABY, YOUNG, ADULT, SENIOR;

        public String toUrlFormatString() {
            if (this == ANY) {
                return "";
            }
            return this.toString();
        }

        public static AgeEnum fromUrlFormatString(String str) {
            for (AgeEnum type : AgeEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ANY;
        }

        @Override
        public String toString() {
            switch (this) {

                case BABY:
                    return "Baby";
                case YOUNG:
                    return "Young";
                case ADULT:
                    return "Adult";
                case SENIOR:
                    return "Senior";
                case ANY:
                default:
                    return "Any";
            }
        }

    }

}
