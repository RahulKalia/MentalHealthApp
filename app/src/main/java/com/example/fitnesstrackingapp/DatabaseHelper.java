package com.example.fitnesstrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Date;

import static java.sql.Types.INTEGER;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    Float stepCount;

    /* Inner class that defines the table contents */
    public static class FeedEntry {
        // Creating the table below
        private static final String userTable = "user_table",
                COL1 = "UID",
                COL2 = "name",
                COL3 = "occupation",
                COL4 = "DOB",
                COL5 = "activityLevel";
    }

    public DatabaseHelper(Context context){
        super(context, FeedEntry.userTable, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating the userTable
        String createUserTable = "CREATE TABLE " + FeedEntry.userTable + " ( UID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FeedEntry.COL2 +" TEXT, " +
                FeedEntry.COL3 +" TEXT, " +
                FeedEntry.COL4 +" TEXT, " +
                FeedEntry.COL5 +" TEXT)" ;
        db.execSQL(createUserTable);

        // Creating the userStepsTable
        String createUserStepsTable = "CREATE TABLE userStepsTable (\n" +
                "  stepDate DATE, \n" +
                "  quarterID INTEGER, \n" +
                "  steps FLOAT,\n" +
                "  UID INTEGER, \n" +
                "  PRIMARY KEY (UID, stepDate, quarterID),\n" +
                "  FOREIGN KEY (UID) REFERENCES user_table(UID)\n" +
                ");" ;
        db.execSQL(createUserStepsTable);

        // Creating the userMoodTable
        String createUserMoodTable = "CREATE TABLE userMoodTable (\n" +
                "  moodDate DATE,\n" +
                "  quarterID INTEGER,\n" +
                "  mood INTEGER,\n" + // Integer between 1 - 5
                "  UID INTEGER,\n" +
                "  PRIMARY KEY (UID, moodDate, quarterID),\n" +
                "  FOREIGN KEY (UID) REFERENCES user_table(UID)\n" +
                ");";
        db.execSQL(createUserMoodTable);

        // Creating Moods Array
        String[] moods = {"Sad", "Moderately Sad", "Not Sad nor Happy", "Moderately Happy", "Happy"};

        // Creating the userActivitiesTable
        String createUserActivitiesTable = "CREATE TABLE userActivitiesTable (\n" +
                "  AID INTEGER, \n" +
                "  dayOfWeekPlayed INTEGER, \n" +
                "  TimePlayed INTEGER, \n" +
                "  intensityLevel INTEGER,\n" +
                "  UID INTEGER, \n" +
                "  PRIMARY KEY (UID, AID),\n" +
                "  FOREIGN KEY (UID) REFERENCES user_table(UID)\n" +
                ");";
        db.execSQL(createUserActivitiesTable);

        // Creating Intensity Array
        String[] intensityLevels = {"Light", "Moderately Light", "Moderate", "Moderately Vigorous", "Vigorous"};

        // Creating DND Table for Do Not Disturb Times
        String createUserDNDTable = "CREATE TABLE userDNDTable (\n" +
                "  DNDID INTEGER, \n" +
                "  day INTEGER, \n" +
                "  DNDPeriods INTEGER,\n" + // 1 to 24 to represent hour in the day (E.g. 12 = 1200 - 1300 DND selected)
                "  UID INTEGER,\n" +
                "  PRIMARY KEY(DNDID),\n" +
                "  FOREIGN KEY(UID) REFERENCES user_table(UID)\n" +
                ");";
        db.execSQL(createUserDNDTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.userTable);
        onCreate(db);
    }

    // For String items
    public boolean addRegisterData(String tableName, String name, String occupation, String DOB, String activityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FeedEntry.COL2, name);
        contentValues.put(FeedEntry.COL3, occupation);
        contentValues.put(FeedEntry.COL4, DOB);
        contentValues.put(FeedEntry.COL5, activityLevel);
        //contentValues.put(FeedEntry.COL6, numTimePlay);
        //contentValues.put(FeedEntry.COL7, startDND);
        //contentValues.put(FeedEntry.COL8, endDND);


        Log.d(TAG, "addRegisterData: Adding " +
                name + occupation + DOB + activityLevel +" to " + tableName);

        long result = db.insert(tableName, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + FeedEntry.userTable;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public String getColumn(String colName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + colName + " FROM " + FeedEntry.userTable  + " WHERE " + FeedEntry.COL1 + " = 1" ;

        Cursor data = db.rawQuery(query,null);
        String item = null;
        while(data.moveToNext()){
            item = data.getString(0);
        }

        return item;
    }

    /**
     * Updates all fields in userTable
     * @param newName
     * @param occupation
     * @param dob
     * @param activityLevel
     */
    public void updateUserTable(String newName, String occupation, String activityLevel, String dob){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + "user_table" + " SET " + FeedEntry.COL2 + " = '" + newName + "',"
                                                        +FeedEntry.COL3 + " = '" + occupation + "',"
                                                        +FeedEntry.COL4 + " = '" + dob+ "',"
                                                        +FeedEntry.COL5 + " = '" + activityLevel + "'"
                                                        +" WHERE " + FeedEntry.COL1 + " = " + '1'  ; // only one user using application so userId will always be 1
        Log.d(TAG, "updateUserTable: query: " + query);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " +
                FeedEntry.userTable +
                " WHERE " +
                FeedEntry.COL1 + " = '" + id + "'" + " " +
                "AND " + FeedEntry.COL2 + " = '" + name + "'";

        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

    public boolean addStepCounterData(String date, int quarterID, float steps, int UID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("stepDate", date);
        contentValues.put("quarterID", quarterID);
        contentValues.put("steps", steps);
        contentValues.put("UID", UID);


        Log.d(TAG, "addRegisterData: Adding " +
                date +" "+ quarterID +" "+ steps +" "+ UID +" to " + "userStepsTable");

        long result = db.insert("userStepsTable", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void updateCurrentValue(float newVal){
        stepCount = newVal;
    }

    // Pass in time as 24 hour four digit number and function returns quarter it is in
    public int calculateQuarterID(char[] timeArr){
        int time = 0;
        // Arrives in format HH:MM -- converted to a 4 digit number for processing
        for(int i=0; i < timeArr.length; i++){
            if(i!= 2){
                time = time *10;
                time += Character.getNumericValue(timeArr[i]);
            }
        }

        int quarterID = -1;
        if (time < 1200){
            if (time >= 800) quarterID = 3;
            if (time >= 400 && time < 800) quarterID = 2;
            else quarterID = 1;
        }
        if (time > 1200){
            if (time <= 1600) quarterID = 4;
            if (time <= 2000 && time > 1600) quarterID = 5;
            else quarterID = 6;
        }
        else quarterID= 3;

        return  quarterID;
    }

    public int getLastSteps(){
        int item = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT steps FROM userStepsTable ORDER BY steps DESC LIMIT 1;" ;
        try {
            Cursor data = db.rawQuery(query,null);
            while(data.moveToNext()){
                item = data.getInt(3);
            }
            return item;
        }catch (Exception se) {
            Log.e(TAG, "getLastSteps: ", se);
        }
        return item;
    }
}
