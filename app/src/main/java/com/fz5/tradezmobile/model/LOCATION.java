package com.fz5.tradezmobile.model;

public enum LOCATION {

    ONLINE("Image"),
    OFFLINE("Video");

    String value;

    LOCATION(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

}
