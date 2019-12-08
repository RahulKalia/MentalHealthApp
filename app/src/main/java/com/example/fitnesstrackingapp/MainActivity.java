package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    TextView textView ;
    Button bRegister, bSettings, bStepCounter;
    static UserLocalStore localStore;

    // When activity is created create the local store and set button on click listener.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRegister = (Button) findViewById(R.id.bRegister);
        bSettings = (Button) findViewById(R.id.bSettings);
        bStepCounter = (Button) findViewById(R.id.bStepCounter);

        bSettings.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        bStepCounter.setOnClickListener(this);

        localStore = new UserLocalStore(this);
    }

    // On starting check if a user exists and if not redirect to the Register page.
    @Override
    protected void onStart(){
        super.onStart();
        if (authenticate() == true){
            displayUserDetails();
            bRegister.setVisibility(View.GONE);
        }else{
            startActivity(new Intent(this, Register.class));
        }
    }

    // Method to check userExists
    private boolean authenticate(){
        return localStore.getUserCreated();
    }

    // Method to display User Details - only used if user has been authenticated to exist.
    // TO BE CHANGED - additional features to be added here.
    private void displayUserDetails(){
        String name = localStore.getUser().name.toString();
        if (name == null){
            etName.setText("Name is null - please update.");
        }else{
            ((TextView)findViewById(R.id.textView)).setText("Welcome back: " + name);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.bSettings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.bStepCounter:
                startActivity(new Intent(this, StepSensor.class));
                break;
        }
    }
}
