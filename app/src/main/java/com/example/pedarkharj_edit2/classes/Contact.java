package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;

public class Contact {

    int id;
    Bitmap imgBitmap;
    String name;

    String created_at;

    // constructors
    public Contact() {
    }

    public Contact(String name, int age) {
        this.name = name;
    }

    public Contact(int id, String name, Bitmap imgBitmap) {
        this.id = id;
        this.name = name;
        this.imgBitmap = imgBitmap;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }
    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public Bitmap getImgBitmap() {
        return imgBitmap;
    }
    public String getCreated_at() {
        return created_at;
    }
}