package com.example.afinal.fingerPrint_Login.oop;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class UserTimeStamp {

    private String notes;

    @ServerTimestamp
    Date time;

    public UserTimeStamp(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
