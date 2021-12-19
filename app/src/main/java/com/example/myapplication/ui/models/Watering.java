package com.example.myapplication.ui.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Watering {
    private String plant;
    private String user_id;
    private String date;
    private String watering_id;
    private String note;
    private String plant_id;
    private @ServerTimestamp Date timestamp;

    public Watering() {
    }

    public Watering(String plant, String user_id, String date, String watering_id, String note, String plant_id, Date timestamp) {
        this.plant = plant;
        this.user_id = user_id;
        this.date = date;
        this.watering_id = watering_id;
        this.note = note;
        this.plant_id = plant_id;
        this.timestamp = timestamp;
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

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
