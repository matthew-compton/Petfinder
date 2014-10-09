package com.ambergleam.petfinder.model;

public enum AnimalType {

    BARNYARD, BIRD, CAT, DOG, HORSE, PIG, REPTILE, SMALLFURRY, ALL;

    public String getString() {
        return this.name().toLowerCase();
    }

    public AnimalType fromString(String str) {
        for (AnimalType type : AnimalType.values()) {
            if (type.getString().equals(str.toLowerCase())) {
                return type;
            }
        }
        return ALL;
    }

}