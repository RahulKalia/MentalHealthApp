package com.example.fitnesstrackingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity implements View.OnClickListener {
    Button bSave;

    EditText etName,etOccupation, etActivitiesPlayed, etAge, etNumTimesWeekPlayed, etStartDND, etEndDND;

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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bSave:
                break;
        }
    }
}
