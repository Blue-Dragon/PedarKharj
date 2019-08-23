package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;

public class Contact {
    private Bitmap bitmap;
    private String name;

    //NO pic
    public Contact(String name) {
        this.name = name;
    }
    //with pic
    public Contact(Bitmap bitmap, String name) {
        this.bitmap = bitmap;
        this.name = name;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
