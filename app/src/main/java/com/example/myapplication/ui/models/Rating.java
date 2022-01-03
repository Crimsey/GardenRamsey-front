package com.example.myapplication.ui.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Rating {

    private String rating_id;
    private float rate;
    private String user_id_owner;
    private String plant_id;
    private String plant_name;
    private String comment;
    private String user_id_rating;
    private String user_name_rating;
    private @ServerTimestamp Date timestamp;

    public Rating() {
    }

    public Rating(String rating_id, float rate, String user_id_owner, String plant_id, String plant_name, String comment, String user_id_rating, String user_name_rating, Date timestamp) {
        this.rating_id = rating_id;
        this.rate = rate;
        this.user_id_owner = user_id_owner;
        this.plant_id = plant_id;
        this.plant_name = plant_name;
        this.comment = comment;
        this.user_id_rating = user_id_rating;
        this.user_name_rating = user_name_rating;
        this.timestamp = timestamp;
    }

    public String getRating_id() {
        return rating_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getUser_id_owner() {
        return user_id_owner;
    }

    public void setUser_id_owner(String user_id_owner) {
        this.user_id_owner = user_id_owner;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id_rating() {
        return user_id_rating;
    }

    public void setUser_id_rating(String user_id_rating) {
        this.user_id_rating = user_id_rating;
    }

    public String getUser_name_rating() {
        return user_name_rating;
    }

    public void setUser_name_rating(String user_name_rating) {
        this.user_name_rating = user_name_rating;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
