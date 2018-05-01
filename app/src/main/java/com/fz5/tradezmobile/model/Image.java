package com.fz5.tradezmobile.model;

import android.graphics.Bitmap;

public class Image {

    private Bitmap bitmap;
    private LOCATION location;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LOCATION getLocation() {
        return location;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }

}
