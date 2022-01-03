package com.util.enums;

public enum PlatformType {

    PLAYER(1),
    ADMIN(2);

    private final Integer value;

    PlatformType(Integer value) {
        this.value = value;
    }

    public int value(){
        return value;
    }

    public static PlatformType fromId(int id) {
        switch (id) {
            case 1:
                return PLAYER;
            case 2:
                return ADMIN;
            default:
                throw new IllegalArgumentException("No Platform type with id : " + id + " found.");
        }
    }
}
