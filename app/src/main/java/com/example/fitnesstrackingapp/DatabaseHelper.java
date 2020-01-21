package com.example.fitnesstrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    /* Inner class that defines the table contents */
    public static class FeedEntry {
        // Creating the table below
        private static final String userTable = "user_table",
                COL1 = "ID",
                COL2 = "name",
                COL3 = "occupation",
                COL4 = "activitiesPlayed",
                COL5 = "age",
                COL6 = "numTimesPlayedPerWeek",
                COL7 = "startDND",
                COL8 = "endDND"; //Necessary ?
    }

    public DatabaseHelper(Context context){
        super(context, FeedEntry.userTable, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + FeedEntry.userTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FeedEntry.COL2 +" TEXT, " +
                FeedEntry.COL3 +" TEXT, " +
                FeedEntry.COL4 +" TEXT, " +
                FeedEntry.COL5 +" INTEGER, " +
                FeedEntry.COL6 +" INTEGER, " +
                FeedEntry.COL7 +" INTEGER, " +
                FeedEntry.COL8 +" INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.userTable);
        onCreate(db);
    }

    // For String items
    public boolean addRegisterData(String tableName, String name, String occupation, String activPlayed, Integer age, Integer numTimePlay, Integer startDND, Integer endDND) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FeedEntry.COL2, name);
        contentValues.put(FeedEntry.COL3, occupation);
        contentValues.put(FeedEntry.COL4, activPlayed);
        contentValues.put(FeedEntry.COL5, age);
        contentValues.put(FeedEntry.COL6, numTimePlay);
        contentValues.put(FeedEntry.COL7, startDND);
        contentValues.put(FeedEntry.COL8, endDND);


        Log.d(TAG, "addRegisterData: Adding " +
                name + occupation + activPlayed + age + numTimePlay + startDND + endDND +" to " + tableName);

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
     * @param activitiesPlayed
     * @param age
     * @param numTimesPlayedPerWeek
     * @param startDND
     * @param endDND
     */
    public void updateUserTable(String newName, String occupation, String activitiesPlayed, int age, int numTimesPlayedPerWeek, int startDND, int endDND){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + FeedEntry.userTable + " SET " + FeedEntry.COL2 + " = '" + newName + "',"
                                                        +FeedEntry.COL3 + " = '" + occupation + "',"
                                                        +FeedEntry.COL4 + " = '" + activitiesPlayed+ "',"
                                                        +FeedEntry.COL5 + " = " + age + ","
                                                        +FeedEntry.COL6 + " = " + numTimesPlayedPerWeek + ","
                                                        +FeedEntry.COL7 + " = " + startDND + ","
                                                        +FeedEntry.COL8 + " = " + endDND
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
}