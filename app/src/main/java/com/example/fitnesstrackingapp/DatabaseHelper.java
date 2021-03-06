package com.example.fitnesstrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


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
                "  stepID INTEGER PRIMARY KEY AUTOINCREMENT,  \n" +
                "  stepDate TEXT, \n" +
                "  quarterID INTEGER, \n" +
                "  steps INTEGER,\n" +
                "  UID INTEGER, \n" +
                "  FOREIGN KEY (UID) REFERENCES user_table(UID)\n" +
                ");" ;
        db.execSQL(createUserStepsTable);

        // Creating the userMoodTable
        String createUserMoodTable = "CREATE TABLE userMoodTable (\n" +
                "  MID INTEGER PRIMARY KEY AUTOINCREMENT,  \n" +
                "  moodDate DATE, \n" +
                "  quarterID INTEGER, \n" +
                "  mood INTEGER,\n" +
                "  UID INTEGER, \n" +
                "  FOREIGN KEY (UID) REFERENCES user_table(UID)\n" +
                ");\n";
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

        // Creating Intensity Array  ---- MOVE THIS TO BE HANDLED BY THE FRONT END
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

        // Creating DND Table for Do Not Disturb Times
        String createUserShakeTable = "CREATE TABLE userShakeTable (\n" +
                "  shakeID INTEGER PRIMARY KEY AUTOINCREMENT,  \n" +
                "  shakeDate DATE, \n" +
                "  quarterID INTEGER, \n" +
                "  UID INTEGER, \n" +
                "  FOREIGN KEY (UID) REFERENCES user_table(UID)\n" +
                ");";
        db.execSQL(createUserShakeTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.userTable);
        onCreate(db);
    }

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
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addShakeDetectorData(String date, int quarterID, int UID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("shakeDate", date);
        contentValues.put("quarterID", quarterID);
        contentValues.put("UID", UID);


        Log.d(TAG, "addRegisterData: Adding " +
                date +" "+ quarterID +" "+ UID +" to " + "userShakeTable");

        long result = db.insert("userShakeTable", null, contentValues);
        db.close();

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
        // Arrives in format HH:MM:SS.SSS -- converted to a 4 digit number for processing
        for(int i=0; i < 5; i++){
            if(i!= 2){
                time = time *10;
                time += Character.getNumericValue(timeArr[i]);
            }
        }

        int quarterID = -1;
        if (time < 1200){
            if (time >= 800) quarterID = 3;

            if (time >= 400 && time < 800) {
                quarterID = 2;
            }else {
                quarterID = 1;
            }
        }

        if (time > 1200){
            if (time <= 1600) quarterID = 4;

            if (time <= 2000 && time > 1600) {
                quarterID = 5;
            }
            if (time > 2000 && time <= 2359){
                quarterID = 6;
            }
        }else {
            quarterID= 3;
        }

        return  quarterID;
    }

    // Executes queries to get the count of the total number of steps in a given quarter
    public ArrayList<Integer> getLastSteps(String dateOfSteps){
        ArrayList<Integer> stepCount = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = null;
        for (int i =1; i <=6; i++){
            int item = 0;
            String query = "SELECT stepID, steps, stepDate FROM userStepsTable WHERE quarterID = "+i+" AND stepDate = '"+dateOfSteps+"' ORDER BY stepID DESC LIMIT 1;";
            data = db.rawQuery(query,null);


            boolean inBounds = data.getCount() >= 1;

            if (inBounds){
                data.moveToNext();
                item= data.getInt(1);
                Integer iItem = new Integer(item);
                stepCount.add(iItem);
                Log.d(TAG, ""+item);
            }else{
                stepCount.add(0);
            }
        }

        data.close();

        return stepCount;
    }


    public ArrayList<Integer> getJittersPerDay(String dateOfShake){
        ArrayList<Integer> jitterCount = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = null;
        for (int i =1; i <=6; i++){
            int item = 0;
            String query = "SELECT COUNT(shakeID) FROM userShakeTable WHERE shakeDate = "+ dateOfShake +" ;";
            data = db.rawQuery(query,null);


            boolean inBounds = data.getCount() >= 1;

            if (inBounds){
                data.moveToNext();
                item= data.getInt(0);
                Integer iItem = new Integer(item);
                jitterCount.add(iItem);
                Log.d(TAG, ""+item);
            }else{
                jitterCount.add(0);
            }
        }

        data.close();

        return jitterCount;
    }
    // TO BE CHANGED
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery
                ("SELECT moodDate, AVG(mood) FROM userMoodTable  GROUP BY moodDate;", null);
        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean saveMood(int moodSelection){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        LocalTime timeNow = LocalTime.now();
        char[] time = timeNow.toString().toCharArray();
        // Parsing char array to quarter calculator
        int qt = calculateQuarterID(time);
        LocalDate dateNow = LocalDate.now();
        String dt = dateNow.toString();

        contentValues.put("mood", moodSelection);
        contentValues.put("moodDate", dt);
        contentValues.put("quarterID", qt);
        contentValues.put("UID", 1);




        Log.d(TAG, "addRegisterData: Adding " +
                moodSelection +" "+ qt +" "+ 1 +" to " + "userMoodTable");

        long result = db.insert("userMoodTable", null, contentValues);
        db.close();
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public void populateDummyData(){
//        BayesHelper bayesHelper = new BayesHelper();
//        addStepCounterData("2020/02/28", 1, 19f,1);
//        addStepCounterData("2020/02/28", 2, 154f,1);
//        addStepCounterData("2020/02/28", 3, 800f,1);
//        addStepCounterData("2020/02/28", 4, 942f,1);
//        addStepCounterData("2020/02/28", 5, 1405f,1);
//        addStepCounterData("2020/02/28", 6, 700f,1);
//
//        saveMood(1,"2020/02/28", 1);
//        bayesHelper.updateMatrices(1,19,12);
//        saveMood(2,"2020/02/28", 2);
//        bayesHelper.updateMatrices(2,173,12);
//        saveMood(3,"2020/02/28", 3);
//        bayesHelper.updateMatrices(3,973,12);
//        saveMood(4,"2020/02/28", 4);
//        bayesHelper.updateMatrices(4,1915,12);
//        saveMood(5,"2020/02/28", 5);
//        bayesHelper.updateMatrices(5,3320,12);
//        saveMood(4,"2020/02/28", 6);
//        bayesHelper.updateMatrices(4,4020,12);


//        addStepCounterData("2020/02/29", 1, 1120f,1);
//        addStepCounterData("2020/02/29", 2, 400f,1);
//        addStepCounterData("2020/02/29", 3, 12f,1);
//        addStepCounterData("2020/02/29", 4, 3004f,1);
//        addStepCounterData("2020/02/29", 5, 1503f,1);
//        addStepCounterData("2020/02/29", 6, 11f,1);
//
//        saveMood(4,"2020/02/29", 1);
//        bayesHelper.updateMatrices(4,1120,12);
//        saveMood(3,"2020/02/29", 2);
//        bayesHelper.updateMatrices(3,1520,12);
//        saveMood(4,"2020/02/29", 3);
//        bayesHelper.updateMatrices(4,1532,12);
//        saveMood(5,"2020/02/29", 4);
//        bayesHelper.updateMatrices(5,4536,12);
//        saveMood(5,"2020/02/29", 5);
//        bayesHelper.updateMatrices(5,6039,12);
//        saveMood(3,"2020/02/29", 6);
//        bayesHelper.updateMatrices(3,6050,12);
//
//        addStepCounterData("2020/03/01", 1, 19f,1);
//        addStepCounterData("2020/03/01", 2, 154f,1);
//        addStepCounterData("2020/03/01", 3, 80f,1);
//        addStepCounterData("2020/03/01", 4, 92f,1);
//        addStepCounterData("2020/03/01", 5, 145f,1);
//        addStepCounterData("2020/03/01", 6, 70f,1);
//
//        saveMood(1,"2020/03/01", 1);
//        bayesHelper.updateMatrices(1,19,12);
//        saveMood(3,"2020/03/01", 2);
//        bayesHelper.updateMatrices(3,173,12);
//        saveMood(1,"2020/03/01", 3);
//        bayesHelper.updateMatrices(1,253,12);
//        saveMood(1,"2020/03/01", 4);
//        bayesHelper.updateMatrices(1,345,12);
//        saveMood(2,"2020/03/01", 5);
//        bayesHelper.updateMatrices(2,490,12);
//        saveMood(1,"2020/03/01", 6);
//        bayesHelper.updateMatrices(1,560,12);
//
//
//        addStepCounterData("2020/03/02", 1, 1120f,1);
//        addStepCounterData("2020/03/02", 2, 40f,1);
//        addStepCounterData("2020/03/02", 3, 12f,1);
//        addStepCounterData("2020/03/02", 4, 3004f,1);
//        addStepCounterData("2020/03/02", 5, 153f,1);
//        addStepCounterData("2020/03/02", 6, 1111f,1);
//
//        saveMood(1,"2020/03/02", 1);
//        bayesHelper.updateMatrices(1,1120,21);
//        saveMood(3,"2020/03/02", 2);
//        bayesHelper.updateMatrices(3,1160,21);
//        saveMood(1,"2020/03/02", 3);
//        bayesHelper.updateMatrices(1,1172,32);
//        saveMood(1,"2020/03/02", 4);
//        bayesHelper.updateMatrices(1,4176,23);
//        saveMood(2,"2020/03/02", 5);
//        bayesHelper.updateMatrices(2,4329,34);
//        saveMood(1,"2020/03/02", 6);
//        bayesHelper.updateMatrices(1,5440,23);



    }

    //for testing only
    public boolean saveMood(int moodSelection, String date, int quarter){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("mood", moodSelection);
        contentValues.put("moodDate", date);
        contentValues.put("quarterID", quarter);
        contentValues.put("UID", 1);




        Log.d(TAG, "addRegisterData: Adding " +
                moodSelection +" "+ quarter +" "+ 1 +" to " + "userMoodTable");

        long result = db.insert("userMoodTable", null, contentValues);
        db.close();
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
