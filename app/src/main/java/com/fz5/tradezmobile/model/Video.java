package com.fz5.tradezmobile.model;

import android.graphics.Bitmap;

public class Video implements Media {

    private MEDIA_TYPE type;
    private String name;
    private String path;
    private LOCATION location;

    public Video(MEDIA_TYPE type, String name) {
        this.type = type;
        this.name = name;
    }

    public MEDIA_TYPE getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public LOCATION getLocation() {
        return location;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
