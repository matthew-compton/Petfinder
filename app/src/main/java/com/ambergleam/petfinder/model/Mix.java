package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Mix implements Serializable {

    @SerializedName("$t")
    public String mString;

    public enum GenderEnum {

        YES, NO;

        private static final GenderEnum DEFAULT = NO;

        public String toUrlFormatString() {
            return this.name().toLowerCase();
        }

        public static GenderEnum fromUrlFormatString(String str) {
            for (GenderEnum type : GenderEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return DEFAULT;
        }

        @Override
        public String toString() {
            switch (this) {
                case YES:
                    return "Yes";
                case NO:
                    return "No";
                default:
                    return DEFAULT.toString();
            }
        }
    }

}
