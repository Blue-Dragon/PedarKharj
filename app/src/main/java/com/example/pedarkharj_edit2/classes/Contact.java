package com.example.pedarkharj_edit3.classes;

public class Contact {

    int id;
    String bitmapStr;
    String name;

    String created_at;

    // constructors
    public Contact() {
    }

    public Contact(String name) {
        this.name = name;
    }

    public Contact(String name, String bitmapStr) {
        this.name = name;
        this.bitmapStr = bitmapStr;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBitmapStr(String bitmapStr) {
        this.bitmapStr = bitmapStr;
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
    public String getBitmapStr() {
        return bitmapStr;
    }
    public String getCreated_at() {
        return created_at;
    }
}