package com.ambergleam.petfinder.model;

import java.io.Serializable;

public class Location implements Serializable {

    public enum LocationEnum {

        ANY, STATE, ZIP;

        public String toUrlFormatString() {
            return this.name();
        }

        public static LocationEnum fromUrlFormatString(String str) {
            for (LocationEnum type : LocationEnum.values()) {
                if (type.toUrlFormatString().equals(str)) {
                    return type;
                }
            }
            return ANY;
        }

        @Override
        public String toString() {
            switch (this) {
                case STATE:
                    return "State";
                case ZIP:
                    return "Zip Code";
                case ANY:
                default:
                    return "Any";
            }
        }

    }

}
