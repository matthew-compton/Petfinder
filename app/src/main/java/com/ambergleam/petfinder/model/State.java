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

        ANY,
        AL,
        AK,
        AZ,
        AR,
        CA,
        CO,
        CT,
        DE,
        FL,
        GA,
        HI,
        ID,
        IL,
        IN,
        IA,
        KS,
        KY,
        LA,
        ME,
        MD,
        MA,
        MI,
        MN,
        MS,
        MO,
        MT,
        NE,
        NV,
        NH,
        NJ,
        NM,
        NY,
        NC,
        ND,
        OH,
        OK,
        OR,
        PA,
        RI,
        SC,
        SD,
        TN,
        TX,
        UT,
        VT,
        VA,
        WA,
        WV,
        WI,
        WY;

        public String toUrlFormatString() {
            return this.name();
        }

        public static StateEnum fromUrlFormatString(String str) {
            for (StateEnum type : StateEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ANY;
        }

        @Override
        public String toString() {
            if (this == ANY) {
                return "Any";
            } else {
                return this.name();
            }
        }

    }

}