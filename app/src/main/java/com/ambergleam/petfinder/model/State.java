package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class State implements Serializable {

    @SerializedName("$t")
    public String mString;

    public String toString() {
        return mString;
    }

    public enum StateEnum {

        UNSPECIFIED,
        AL,
        GA;

        public String toUrlFormatString() {
            return this.name();
        }

        public static StateEnum fromUrlFormatString(String str) {
            for (StateEnum type : StateEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return UNSPECIFIED;
        }

        @Override
        public String toString() {
            return this.name().toString();
        }

    }

}