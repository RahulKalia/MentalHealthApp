package com.example.fitnesstrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity implements View.OnClickListener {
    Button bSave;

    EditText etName,etOccupation, etActivitiesPlayed, etAge, etNumTimesWeekPlayed, etStartDND, etEndDND;

    UserLocalStore localStore;

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

        localStore = new UserLocalStore(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bSave:

                String name = etName.getText().toString();
                String occupation = etOccupation.getText().toString();
                String activitiesPlayed = etActivitiesPlayed.getText().toString();

                int age = Integer.parseInt(etAge.getText().toString());
                int numTimesWeekPlayed = Integer.parseInt(etNumTimesWeekPlayed.getText().toString());
                int startDND = Integer.parseInt(etStartDND.getText().toString());
                int endDND = Integer.parseInt(etEndDND.getText().toString());

                User registeredData = new User(name, occupation, activitiesPlayed, age, numTimesWeekPlayed, startDND, endDND);


                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
