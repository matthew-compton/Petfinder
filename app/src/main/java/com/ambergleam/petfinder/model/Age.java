package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Age implements Serializable {

    @SerializedName("$t")
    public String mString;


    public enum AgeEnum {

        BABY, YOUNG, ADULT, SENIOR;

        private static final AgeEnum DEFAULT = ADULT;

        public String toUrlFormatString() {
            return this.toString();
        }

        public static AgeEnum fromUrlFormatString(String str) {
            for (AgeEnum type : AgeEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return DEFAULT;
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
                default:
                    return DEFAULT.toString();
            }
        }

    }

}
