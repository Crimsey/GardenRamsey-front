package com.example.myapplication.ui.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Watering {
    private String plant;
    private String user_id;
    private String date;
    private String watering_id;
    private String note;

    public Watering() {
    }

    public Watering(String plant, String user_id, String date, String watering_id, String note) {
        this.plant = plant;
        this.user_id = user_id;
        this.date = date;
        this.watering_id = watering_id;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWatering_id() {
        return watering_id;
    }

    public void setWatering_id(String watering_id) {
        this.watering_id = watering_id;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
