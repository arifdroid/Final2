package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

class EntryEvening {

    private String name;
    private String uid;
    private ArrayList<Entry> entryArrayList;

    public EntryEvening() {

    }

    public EntryEvening(String name, ArrayList<Entry> entryArrayList) {
        this.name = name;
        this.entryArrayList = entryArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<Entry> getEntryArrayList() {
        return entryArrayList;
    }

    public void setEntryArrayList(ArrayList<Entry> entryArrayList) {
        this.entryArrayList = entryArrayList;
    }
}
