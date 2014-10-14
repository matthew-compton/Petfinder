package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Size implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return SizeEnum.fromUrlFormatString(mString).toString();
    }

    public enum SizeEnum {

        ANY, S, M, L, XL;

        public String toUrlFormatString() {
            if (this == ANY) {
                return "";
            }
            return this.name();
        }

        public static SizeEnum fromUrlFormatString(String str) {
            for (SizeEnum type : SizeEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ANY;
        }

        @Override
        public String toString() {
            switch (this) {
                case S:
                    return "Small";
                case M:
                    return "Medium";
                case L:
                    return "Large";
                case XL:
                    return "Extra-Large";
                case ANY:
                default:
                    return "Any";
            }
        }

    }

}
