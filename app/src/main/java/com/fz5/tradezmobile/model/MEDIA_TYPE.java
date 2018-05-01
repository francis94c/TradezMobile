package com.fz5.tradezmobile.model;

public enum MEDIA_TYPE {

    IMAGE("Image"),
    VIDEO("Video");

    String value;

    MEDIA_TYPE(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

}
