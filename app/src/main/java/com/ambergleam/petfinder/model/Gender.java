package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gender implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return GenderEnum.fromUrlFormatString(mString).toString();
    }

    public enum GenderEnum {

        M, F, U, NONE;

        public String toUrlFormatString() {
            return this.name();
        }

        public static GenderEnum fromUrlFormatString(String str) {
            for (GenderEnum type : GenderEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return NONE;
        }

        @Override
        public String toString() {
            switch (this) {
                case M:
                    return "Male";
                case F:
                    return "Female";
                case U:
                    return "Unknown";
                case NONE:
                default:
                    return "";
            }
        }
    }

}