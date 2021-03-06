package com.example.pedarkharj_edit3.classes.models;


public class Event {

    int id;
    String bitmapStr;
    String event_name;
    String created_at;

    // constructors
    public Event() {

    }

    public Event(String event_name) {
        this.event_name = event_name;
    }

    public Event(int id, String event_name) {
        this.id = id;
        this.event_name = event_name;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }
    public void setEventName(String eventName) {
        this.event_name = eventName;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public void setBitmapStr(String bitmapStr) {
        this.bitmapStr = bitmapStr;
    }

    // getter
    public int getId() {
        return this.id;
    }
    public String getEventName() {
        return this.event_name;
    }
    public String getCreated_at() {
        return created_at;
    }
    public String getBitmapStr() {
        return bitmapStr;
    }
}