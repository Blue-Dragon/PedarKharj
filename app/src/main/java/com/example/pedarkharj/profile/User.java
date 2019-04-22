package com.example.pedarkharj.profile;

import android.graphics.Bitmap;

public class User {
    private int id, picUpdateNum;
    private String name, email, gender;
    private Bitmap bitmap;

    public User(int id, String name, String email, String gender, Bitmap bitmap, int picUpdateNum) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.bitmap = bitmap;
        this.picUpdateNum = picUpdateNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getPicUpdateNum() {
        return picUpdateNum;
    }

    public void setPicUpdateNum(int picUpdateNum) {
        this.picUpdateNum = picUpdateNum;
    }
}
