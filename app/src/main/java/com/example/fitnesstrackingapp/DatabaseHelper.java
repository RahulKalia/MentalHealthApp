package com.example.fitnesstrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Creating the table below
    private static final String userTable = "user_table", COL1 = "ID", COL2 = "name", COL3 = "occupation";
    private static final String COL4 = "activitiesPlayed"; //Not sure what we gain from this
    private static final String COL5 = "age";
    private static final String COL6 = "numTimesPlayedPerWeek"; //Not sure if needed
    private static final String COL7 = "startDND"; //Necessary ?
    private static final String COL8 = "endDND"; //Necessary ?


    public DatabaseHelper(Context context){
        super(context, userTable, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + userTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT, " + COL3 +" TEXT, " + COL4 +" TEXT, " + COL5 +" INTEGER, " + COL6 +" INTEGER, " + COL7 +" INTEGER, " + COL8 +" INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + userTable);
        onCreate(db);
    }

    // For String items
    public boolean addData(String tableName, String colName, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(colName, item);

        Log.d(TAG, "addData: Adding " + item + " to " + tableName);

        long result = db.insert(tableName, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //For Integer items
    public boolean addData(String tableName, String colName, int item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(colName, item);

        Log.d(TAG, "addData: Adding " + item + " to " + tableName);

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
        String query = "SELECT * FROM " + userTable;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public String getColumn(String colName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + colName + " FROM " + userTable  + " WHERE " + COL1 + " = 1" ;

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
        String query = "UPDATE " + userTable + " SET " + COL2 + " = '" + newName + "'"
                                                        +COL3 + " = '" + occupation + "'"
                                                        +COL4 + " = '" + activitiesPlayed+ "'"
                                                        +COL5 + " = " + age
                                                        +COL6 + " = " + numTimesPlayedPerWeek
                                                        +COL7 + " = " + startDND
                                                        +COL8 + " = " + endDND
                                                        +"' WHERE " + COL1 + " = " + '1'  ; // only one user using application so userId will always be 1
        Log.d(TAG, "updateUserTable: query: " + query);
        //Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + userTable + " WHERE " + COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
}
