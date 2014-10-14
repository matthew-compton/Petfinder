package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Mix implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return MixEnum.fromUrlFormatString(mString).toString();
    }

    public enum MixEnum {

        ANY, YES, NO;

        public String toUrlFormatString() {
            if (this == ANY) {
                return "";
            }
            return this.name().toLowerCase();
        }

        public static MixEnum fromUrlFormatString(String str) {
            for (MixEnum type : MixEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ANY;
        }

        @Override
        public String toString() {
            switch (this) {
                case YES:
                    return "Yes";
                case NO:
                    return "No";
                case ANY:
                default:
                    return "Any";
            }
        }

    }

}
