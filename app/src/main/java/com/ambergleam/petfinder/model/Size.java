package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Size implements Serializable {

    @SerializedName("$t")
    public String mString;

    public enum SizeEnum {

        S, M, L, XL;

        private static final SizeEnum DEFAULT = M;

        public String toUrlFormatString() {
            return this.name();
        }

        public static SizeEnum fromUrlFormatString(String str) {
            for (SizeEnum type : SizeEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return DEFAULT;
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
                default:
                    return DEFAULT.toString();
            }
        }

    }

}
