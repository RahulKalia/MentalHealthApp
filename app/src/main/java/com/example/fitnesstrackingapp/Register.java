package com.example.fitnesstrackingapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity implements View.OnClickListener {
    Button bSave;

    EditText etName,etOccupation, etActivitiesPlayed, etAge, etNumTimesWeekPlayed, etStartDND, etEndDND;

    UserLocalStore localStore; // TO DELETE

    private static final String TAG = "Register";

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etOccupation = (EditText) findViewById(R.id.etOccupation);
        etActivitiesPlayed = (EditText) findViewById(R.id.etActivitiesPlayed);
        etAge = (EditText) findViewById(R.id.etAge);
        etNumTimesWeekPlayed = (EditText) findViewById(R.id.etNumTimesWeekPlayed);
        etStartDND = (EditText) findViewById(R.id.etStartDND);
        etEndDND = (EditText) findViewById(R.id.etEndDND);

        bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelper(this);

        localStore = new UserLocalStore(getApplicationContext()); // TO DELETE
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bSave:

              /*  if (validate() == true){
                    String name = etName.getText().toString();
                    String occupation = etOccupation.getText().toString();
                    String activitiesPlayed = etActivitiesPlayed.getText().toString();

                    int age = Integer.parseInt(etAge.getText().toString());
                    int numTimesWeekPlayed = Integer.parseInt(etNumTimesWeekPlayed.getText().toString());
                    int startDND = Integer.parseInt(etStartDND.getText().toString());
                    int endDND = Integer.parseInt(etEndDND.getText().toString());

                    User registeredData = new User(name, occupation, activitiesPlayed, age, numTimesWeekPlayed, startDND, endDND);


                    localStore.storeUserData(registeredData);

                    localStore.setUserCreated(true);

                    Toast.makeText(getApplicationContext(),"Success - user has been created.",Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Error - please complete all fields.",Toast.LENGTH_SHORT).show();
                }*/


                if (validate() == true) {
                    String name = etName.getText().toString();
                    String occupation = etOccupation.getText().toString();
                    String activitiesPlayed = etActivitiesPlayed.getText().toString();

                    int age = Integer.parseInt(etAge.getText().toString());
                    int numTimesWeekPlayed = Integer.parseInt(etNumTimesWeekPlayed.getText().toString());
                    int startDND = Integer.parseInt(etStartDND.getText().toString());
                    int endDND = Integer.parseInt(etEndDND.getText().toString());



                    // BELOW TO BE COMPLETED - ADAPT TO FIT CURRENT INPUT STRUCTURE
                    mDatabaseHelper.addData("user_table","name",name);
                    mDatabaseHelper.addData("user_table","occupation",occupation);
                    mDatabaseHelper.addData("user_table","activitiesPlayed",activitiesPlayed);
                    mDatabaseHelper.addData("user_table","age",age);
                    mDatabaseHelper.addData("user_table","numTimesPlayedPerWeek",numTimesWeekPlayed);
                    mDatabaseHelper.addData("user_table", "startDND", startDND);
                    mDatabaseHelper.addData("user_table", "endDND", endDND);

                    toastMessage("Data Successfully Inserted!");

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    toastMessage("You must put something in the text field!");
                }
        }
    }


    public boolean validate(){
        String sName = etName.getText().toString();
        String sOccupation = etOccupation.getText().toString();
        String sActivitiesPlayed = etActivitiesPlayed.getText().toString();
        String iAge = etAge.getText().toString();
        String iNumTimesWeek = etNumTimesWeekPlayed.getText().toString();
        String iStartDND = etStartDND.getText().toString();
        String iEndDND= etEndDND.getText().toString();
        boolean result = false;

        if (sName.isEmpty()) {
            etName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (sOccupation.isEmpty()) {
            etOccupation.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (sActivitiesPlayed.isEmpty()) {
            etActivitiesPlayed.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (iAge.isEmpty() ) {
            etAge.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (iNumTimesWeek.isEmpty() ) {
            etNumTimesWeekPlayed.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (iStartDND.isEmpty() ) {
            etStartDND.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (iEndDND.isEmpty()) {
            etEndDND.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }else{
            result = true;
        }
        return result;
    }

    /*public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }/

    /**
     * customizable toast
     * @param message
     */

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
