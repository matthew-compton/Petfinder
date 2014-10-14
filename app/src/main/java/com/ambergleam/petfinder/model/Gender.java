package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gender implements Serializable {

    @SerializedName("$t")
    public String mString;

    public enum GenderEnum {

        M, F;

        private static final GenderEnum DEFAULT = M;

        public String toUrlFormatString() {
            return this.name();
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
                case M:
                    return "Male";
                case F:
                    return "Female";
                default:
                    return DEFAULT.toString();
            }
        }
    }

}