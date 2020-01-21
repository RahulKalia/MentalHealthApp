package com.example.fitnesstrackingapp;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bUpdate;

    EditText etName,etOccupation, etActivitiesPlayed, etAge, etNumTimesWeekPlayed, etStartDND, etEndDND;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etName = (EditText) findViewById(R.id.etName);
        etOccupation = (EditText) findViewById(R.id.etOccupation);
        etActivitiesPlayed = (EditText) findViewById(R.id.etActivitiesPlayed);
        etAge = (EditText) findViewById(R.id.etAge);
        etNumTimesWeekPlayed = (EditText) findViewById(R.id.etNumTimesWeekPlayed);
        etStartDND = (EditText) findViewById(R.id.etStartDND);
        etEndDND = (EditText) findViewById(R.id.etEndDND);

        bUpdate = (Button) findViewById(R.id.bUpdate);
        bUpdate.setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelper(this);

        displayUserDetails();

    }


    private void displayUserDetails(){
        Cursor data  = mDatabaseHelper.getData();

        ArrayList<String> listData = new ArrayList<>();

        while(data.moveToNext()){
            //get the value from the database in column 1 (which is name)
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }

        ((EditText)findViewById(R.id.etName)).setText(mDatabaseHelper.getColumn("name"));
        String name = listData.get(0);
        ((EditText)findViewById(R.id.etOccupation)).setText(mDatabaseHelper.getColumn("occupation"));
        String occ = mDatabaseHelper.getColumn("occupation");
        ((EditText)findViewById(R.id.etActivitiesPlayed)).setText(mDatabaseHelper.getColumn("activitiesPlayed"));
        String ap = mDatabaseHelper.getColumn("activitiesPlayed");
        ((EditText)findViewById(R.id.etAge)).setText(mDatabaseHelper.getColumn("age"));
        String age = mDatabaseHelper.getColumn("age");
        ((EditText)findViewById(R.id.etNumTimesWeekPlayed)).setText(mDatabaseHelper.getColumn("numTimesPlayedPerWeek"));
        String ntppw = mDatabaseHelper.getColumn("numTimesPlayedPerWeek");
        ((EditText)findViewById(R.id.etStartDND)).setText(mDatabaseHelper.getColumn("startDND"));
        String startDND = mDatabaseHelper.getColumn("startDND");
        ((EditText)findViewById(R.id.etEndDND)).setText(mDatabaseHelper.getColumn("endDND"));
        String end = mDatabaseHelper.getColumn("endDND");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bUpdate:
                String name = etName.getText().toString();
                String occupation = etOccupation.getText().toString();
                String activitiesPlayed = etActivitiesPlayed.getText().toString();

                int age = Integer.parseInt(etAge.getText().toString());
                int numTimesWeekPlayed = Integer.parseInt(etNumTimesWeekPlayed.getText().toString());
                int startDND = Integer.parseInt(etStartDND.getText().toString());
                int endDND = Integer.parseInt(etEndDND.getText().toString());

                mDatabaseHelper.updateUserTable(name, occupation, activitiesPlayed, age, numTimesWeekPlayed, startDND, endDND);


                Toast.makeText(getApplicationContext(),"Success - user has been updated.",Toast.LENGTH_SHORT).show();
        }
    }
}
