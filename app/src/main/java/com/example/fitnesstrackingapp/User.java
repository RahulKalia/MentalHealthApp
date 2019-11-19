package com.example.fitnesstrackingapp;

public class User {

    String name, occupation, activitiesPlayed;
    int age, numTimesWeekPlayed;
    int startDoNotDisturb, endDoNotDisturb;

    public User(String name, String occupation, String activitiesPlayed, int age, int numTimesWeekPlayed, int startDoNotDisturb, int endDoNotDisturb){
        this.name = name;
        this.occupation = occupation;
        this.activitiesPlayed = activitiesPlayed;
        this.age = age;
        this.numTimesWeekPlayed = numTimesWeekPlayed;
        this.startDoNotDisturb = startDoNotDisturb;
        this.endDoNotDisturb = endDoNotDisturb;
    }

}
