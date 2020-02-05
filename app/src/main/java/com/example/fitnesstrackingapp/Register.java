package com.example.fitnesstrackingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.util.Date;
import java.util.Calendar;


public class Register extends AppCompatActivity implements View.OnClickListener {
    Button bSave;
    EditText etName,etOccupation, etActivitiesPlayed, etNumTimesWeekPlayed, etStartDND, etEndDND;
    private TextView mDisplayDate, tvDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "Register";
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etOccupation = (EditText) findViewById(R.id.etOccupation);
        etActivitiesPlayed = (EditText) findViewById(R.id.etActivitiesPlayed);
        etNumTimesWeekPlayed = (EditText) findViewById(R.id.etNumTimesWeekPlayed);
        etStartDND = (EditText) findViewById(R.id.etStartDND);
        etEndDND = (EditText) findViewById(R.id.etEndDND);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        tvDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelper(this);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSetListener");
                month = month +1;
                String date = dayOfMonth + "-" +  month + "-" + year;
                Date currDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                LocalDate l1 = LocalDate.of(year, month, dayOfMonth);
                LocalDate now1 = LocalDate.now();

                try {
                    Date date1 =sdf.parse(date);
                    if (date1.compareTo(currDate) > 0){
                        toastMessage("Error: Please select a valid date");
                    }else{
                        Period diff1 = Period.between(l1, now1);
                        mDisplayDate.setText(date);
                        diff1.getYears();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };


        bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bSave:
                if (validate() == true) {
                    String name = etName.getText().toString();
                    String occupation = etOccupation.getText().toString();
                    String activitiesPlayed = etActivitiesPlayed.getText().toString();
                    String dob = mDisplayDate.getText().toString();

                    int numTimesWeekPlayed = Integer.parseInt(etNumTimesWeekPlayed.getText().toString());
                    int startDND = Integer.parseInt(etStartDND.getText().toString());
                    int endDND = Integer.parseInt(etEndDND.getText().toString());


                    // BELOW TO BE COMPLETED - ADAPT TO FIT CURRENT INPUT STRUCTURE
                    mDatabaseHelper.addRegisterData("user_table", name, occupation, activitiesPlayed, dob, numTimesWeekPlayed, startDND, endDND);
                    Log.d(TAG, "onClick: save " + name+ occupation+ activitiesPlayed+ dob+  numTimesWeekPlayed+ startDND + endDND);
                    toastMessage("Data Successfully Inserted!");

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    toastMessage("You must put something in the text field!");
                }
                break;
            case R.id.tvDate:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(this,
                        android.R.style.Theme_Material_Dialog_MinWidth,mDateSetListener,
                        year, month, day);
                dialog.show();
        }
    }

    public boolean validate(){
        String sName = etName.getText().toString();
        String sOccupation = etOccupation.getText().toString();
        String sActivitiesPlayed = etActivitiesPlayed.getText().toString();
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
        if (iNumTimesWeek.isEmpty() ) {
            etNumTimesWeekPlayed.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        }
        if (tvDate.getText() == "Select"){
            tvDate.setTextColor(00); //FIX THIS
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

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
