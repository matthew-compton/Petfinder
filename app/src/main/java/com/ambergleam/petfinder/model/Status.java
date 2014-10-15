package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return StatusEnum.fromUrlFormatString(mString).toString();
    }

    public enum StatusEnum {

        ANY, A, H, P, X, U;

        public String toUrlFormatString() {
            if (this == ANY || this == U) {
                return "";
            }
            return this.name();
        }

        public static StatusEnum fromUrlFormatString(String str) {
            for (StatusEnum type : StatusEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ANY;
        }

        @Override
        public String toString() {
            switch (this) {
                case A:
                    return "Adoptable";
                case H:
                    return "On Hold";
                case P:
                    return "Pending";
                case X:
                    return "Adopted";
                case U:
                    return "Unknown";
                case ANY:
                default:
                    return "Any";
            }
        }

    }

}
