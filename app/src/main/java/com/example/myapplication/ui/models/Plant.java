package com.example.myapplication.ui.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class Plant {
    public List<Plant> plants;

    private String plant_name;
    private String plant_type;
    private String plant_note;
    private String date_plating;
    private String plant_picture;
    private Integer plant_insolation;
    private String soil_humidity;
    private Integer air_humidity;
    private Integer plant_nutrient;
    private boolean plant_is_poison;
    private @ServerTimestamp Date timestamp;
    private Integer plant_irrigation;
    private String plant_id;
    private String user_id;

    public Plant(String plant_name, String plant_type, String plant_note, String date_plating, String plant_picture, Integer plant_insolation, String soil_humidity, Integer air_humidity, Integer plant_nutrient, boolean plant_is_poison, Date timestamp, Integer plant_irrigation, String plant_id, String user_id) {
        this.plant_name = plant_name;
        this.plant_type = plant_type;
        this.plant_note = plant_note;
        this.date_plating = date_plating;
        this.plant_picture = plant_picture;
        this.plant_insolation = plant_insolation;
        this.soil_humidity = soil_humidity;
        this.air_humidity = air_humidity;
        this.plant_nutrient = plant_nutrient;
        this.plant_is_poison = plant_is_poison;
        this.timestamp = timestamp;
        this.plant_id = plant_id;
        this.user_id = user_id;
        this.plant_irrigation = plant_irrigation;
    }

    public Plant(){

    }
    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getPlant_type() {
        return plant_type;
    }

    public void setPlant_type(String plant_type) {
        this.plant_type = plant_type;
    }

    public String getPlant_note() {
        return plant_note;
    }

    public void setPlant_note(String plant_note) {
        this.plant_note = plant_note;
    }

    public String getDate_plating() {
        return date_plating;
    }

    public void setDate_plating(String date_plating) {
        this.date_plating = date_plating;
    }

    public String getPlant_picture() {
        return plant_picture;
    }

    public void setPlant_picture(String plant_picture) {
        this.plant_picture = plant_picture;
    }

    public Integer getPlant_insolation() {
        return plant_insolation;
    }

    public void setPlant_insolation(Integer plant_insolation) {
        this.plant_insolation = plant_insolation;
    }

    public String getSoil_humidity() {
        return soil_humidity;
    }

    public void setSoil_humidity(String soil_humidity) {
        this.soil_humidity = soil_humidity;
    }

    public Integer getAir_humidity() {
        return air_humidity;
    }

    public void setAir_humidity(Integer air_humidity) {
        this.air_humidity = air_humidity;
    }

    public Integer getPlant_nutrient() {
        return plant_nutrient;
    }

    public void setPlant_nutrient(Integer plant_nutrient) {
        this.plant_nutrient = plant_nutrient;
    }

    public boolean isPlant_is_poison() {
        return plant_is_poison;
    }

    public void setPlant_is_poison(boolean plant_is_poison) {
        this.plant_is_poison = plant_is_poison;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getPlant_irrigation() {
        return plant_irrigation;
    }

    public void setPlant_irrigation(Integer plant_irrigation) {
        this.plant_irrigation = plant_irrigation;
    }
}

