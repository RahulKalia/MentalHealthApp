package com.example.fitnesstrackingapp;

public class layoutItem {

    private int mood;
    private String date;

    public layoutItem(String date, int mood) {
        this.mood = mood;
        this.date = date;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
