package com.example.afinal.fingerPrint_Login.oop;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserCheckIn {

    private String name;
    private String phone;
    private String image_url;
   // private String ref_score_card;
    private String uid;
    private float rating;
    private int priority;

    //data for each time stamp
    // maybe we dont have to, we just get time from server, or get it from system as backup,
    //then save as string inside data for easier read.
    @ServerTimestamp
    Date time;

    //private

    public UserCheckIn(){

    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public UserCheckIn(String name, String phone, String image_url, String uid, float rating) {
        this.name = name;
        this.phone = phone;
        this.image_url = image_url;
        this.uid = uid;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
//
//    public String getRef_score_card() {
//        return ref_score_card;
//    }
//
//    public void setRef_score_card(String ref_score_card) {
//        this.ref_score_card = ref_score_card;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
