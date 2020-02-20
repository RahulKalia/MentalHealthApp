package com.example.fitnesstrackingapp;

class MoodItem {
    private int mood;
    private String date;

    public MoodItem(int mood, String date){
        this.mood = mood;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getMood() {
        return mood;
    }

}
