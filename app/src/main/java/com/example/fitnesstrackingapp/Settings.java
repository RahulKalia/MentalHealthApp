package com.example.fitnesstrackingapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bUpdate;

    EditText etName,etOccupation, etActivitiesPlayed, etAge, etNumTimesWeekPlayed, etStartDND, etEndDND;

    UserLocalStore localStore;

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

        localStore = new UserLocalStore(getApplicationContext());

        displayUserDetails();

    }

    private void displayUserDetails(){
        User user = localStore.getUser();
        ((EditText)findViewById(R.id.etName)).setText(user.name.toString());
        ((EditText)findViewById(R.id.etOccupation)).setText(user.occupation.toString());
        ((EditText)findViewById(R.id.etActivitiesPlayed)).setText(user.activitiesPlayed.toString());
        ((EditText)findViewById(R.id.etAge)).setText(Integer.toString(user.age));
        ((EditText)findViewById(R.id.etNumTimesWeekPlayed)).setText(Integer.toString(user.numTimesWeekPlayed));
        ((EditText)findViewById(R.id.etStartDND)).setText(Integer.toString(user.startDoNotDisturb));
        ((EditText)findViewById(R.id.etEndDND)).setText(Integer.toString(user.endDoNotDisturb));
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

                User registeredData = new User(name, occupation, activitiesPlayed, age, numTimesWeekPlayed, startDND, endDND);


                localStore.storeUserData(registeredData);

                localStore.setUserCreated(true);

                Toast.makeText(getApplicationContext(),"Success - user has been updated.",Toast.LENGTH_SHORT).show();
        }
    }
}
