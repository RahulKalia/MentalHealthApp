package com.example.fitnesstrackingapp;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    // Store the user details from the items that are passed in using the user data type.
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("occupation", user.occupation);
        spEditor.putString("activities played", user.activitiesPlayed);
        spEditor.putInt("age", user.age);
        spEditor.putInt("Times Played per Week", user.numTimesWeekPlayed);
        spEditor.putInt("Start DND Time", user.startDoNotDisturb);
        spEditor.putInt("End DND Time", user.endDoNotDisturb);
        spEditor.commit();
    }

    // Gets the user from the Shared Preferences userLocalDatabase.
    public User getUser(){
        String name = userLocalDatabase.getString("name","");
        String occupation = userLocalDatabase.getString("occupation", "");
        String activitiesPlayed = userLocalDatabase.getString("activitiesPlayed", "");
        int age = userLocalDatabase.getInt("age", -1);
        int numTimesWeekPlayed = userLocalDatabase.getInt("Times Played per Week", -1);
        int startDoNotDisturb = userLocalDatabase.getInt("Start DND Time", -1);
        int endDoNotDisturb = userLocalDatabase.getInt("End DND Time", -1);

        User storedUser = new User(name, occupation, activitiesPlayed, age, numTimesWeekPlayed, startDoNotDisturb, endDoNotDisturb);

        return storedUser;
    }

    // Used for testing so we can clear it each time - TO BE REMOVED.
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
