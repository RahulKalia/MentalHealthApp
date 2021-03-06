package com.example.fitnesstrackingapp;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bUpdate;
    EditText etName,etOccupation;
    TextView tvDate;
    Spinner dropdown;

    DatabaseHelper mDatabaseHelper;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etName = (EditText) findViewById(R.id.etName);
        etOccupation = (EditText) findViewById(R.id.etOccupation);

        dropdown = (Spinner) findViewById(R.id.spinner);
        String[] spinnerItems = new String[]{"Very Active", "Moderately Active", "Lightly Active", "Sedentary"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        dropdown.setAdapter(adapter);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);

        bUpdate = (Button) findViewById(R.id.bUpdate);
        bUpdate.setOnClickListener(this);

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
                        tvDate.setText(date);
                        diff1.getYears();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        displayUserDetails();

    }


    private void displayUserDetails(){
        Cursor data  = mDatabaseHelper.getData();

        ((EditText)findViewById(R.id.etName)).setText(mDatabaseHelper.getColumn("name"));
        ((EditText)findViewById(R.id.etOccupation)).setText(mDatabaseHelper.getColumn("occupation"));
        ((TextView)findViewById(R.id.tvDate)).setText(mDatabaseHelper.getColumn("DOB"));
        String activity_level = mDatabaseHelper.getColumn("activityLevel");

        switch (activity_level){
            case "Very Active":
                dropdown.setSelection(1);
                break;
            case "Moderately Active":
                dropdown.setSelection(2);
                break;
            case "Lightly Active":
                dropdown.setSelection(3);
                break;
            case "Sedentary":
                dropdown.setSelection(4);
                break;
            default:
                dropdown.setSelection(2);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bUpdate:
                String name = etName.getText().toString();
                String occupation = etOccupation.getText().toString();
                String activityLevel = dropdown.getSelectedItem().toString();
                String DOB = tvDate.getText().toString();

//                int numTimesWeekPlayed = Integer.parseInt(etNumTimesWeekPlayed.getText().toString());
//                int startDND = Integer.parseInt(etStartDND.getText().toString());
//                int endDND = Integer.parseInt(etEndDND.getText().toString());

                mDatabaseHelper.updateUserTable(name, occupation, activityLevel, DOB);


                Toast.makeText(getApplicationContext(),"Success - user has been updated.",Toast.LENGTH_SHORT).show();
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

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
