package com.example.fitnesstrackingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    // Method stores the user details from the items that are passed in using the user data type.
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("occupation", user.occupation);
        spEditor.putString("activities played", user.activitiesPlayed);
        spEditor.putInt("age", user.age);
        spEditor.putInt("timesPlayedWeek", user.numTimesWeekPlayed);
        spEditor.putInt("startDNDTime", user.startDoNotDisturb);
        spEditor.putInt("endDNDTime", user.endDoNotDisturb);

        setUserCreated(true);

        spEditor.commit();
        Log.d("FYP", "Successfully committed");
    }

    // Method gets the user from the Shared Preferences userLocalDatabase.
    public User getUser(){
        String name = userLocalDatabase.getString("name","");
        String occupation = userLocalDatabase.getString("occupation", "");
        String activitiesPlayed = userLocalDatabase.getString("activitiesPlayed", "");
        int age = userLocalDatabase.getInt("age", -1);
        int numTimesWeekPlayed = userLocalDatabase.getInt("timesPlayedWeek", -1);
        int startDoNotDisturb = userLocalDatabase.getInt("startDNDTime", -1);
        int endDoNotDisturb = userLocalDatabase.getInt("endDNDTime", -1);

        User storedUser = new User(name, occupation, activitiesPlayed, age, numTimesWeekPlayed, startDoNotDisturb, endDoNotDisturb);

        return storedUser;
    }

    // Method for testing so we can clear it each time - TO BE REMOVED.
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    // Method to check if a user has been configured and therefore can direct the person to the correct activity.
    public boolean getUserCreated(){
        if (userLocalDatabase.getBoolean("userCreated", false) == true){
            return true;
        }else{
            return false;
        }
    }

    public void setUserCreated(boolean userCreated){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("userCreated", userCreated);
        spEditor.commit();
    }





}
