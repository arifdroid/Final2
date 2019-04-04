package com.example.afinal.fingerPrint_Login.oop;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class EntryMorning {

    private String name; //for testing purpose
    private String uid; // better design , more accurate.

    private Entry entry;

    private ArrayList<Entry> entryArrayList;

    public EntryMorning(String name, ArrayList<Entry> entryArrayList) {
        this.name = name;
        this.entryArrayList = entryArrayList;
    }

    public ArrayList<Entry> getEntryArrayList() {
        return entryArrayList;
    }

    public void setEntryArrayList(ArrayList<Entry> entryArrayList) {
        this.entryArrayList = entryArrayList;
    }

    public EntryMorning(String name, Entry entry) {
        this.name = name;
        this.entry = entry;
    }

    public EntryMorning(Entry entry) {
        this.entry = entry;
    }

    public EntryMorning() {

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

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
