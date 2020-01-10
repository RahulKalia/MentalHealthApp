package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    TextView textView ;
    Button bRegister, bSettings, bStepCounter , bTest;
    static UserLocalStore localStore;
    DatabaseHelper mDatabaseHelper;


    // When activity is created create the local store and set button on click listener.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRegister = (Button) findViewById(R.id.bRegister);
        bSettings = (Button) findViewById(R.id.bSettings);
        bStepCounter = (Button) findViewById(R.id.bStepCounter);
        bTest = (Button) findViewById(R.id.bTest);
        textView = (TextView) findViewById(R.id.textView);

        bSettings.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        bStepCounter.setOnClickListener(this);
        bTest.setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelper(this);
        //localStore = new UserLocalStore(this);
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

    // Method to check user exists
    private boolean authenticate(){
        boolean result = false;

        String name = mDatabaseHelper.getColumn("name");

        if (name != null){
            return true;
        }
        return false;
    }

    // Method to display User Details - only used if user has been authenticated to exist.
    // TO BE CHANGED - additional features to be added here.
    private void displayUserDetails(){
        //String name = localStore.getUser().name.toString();


        String name = mDatabaseHelper.getColumn("name");
        textView.setText(name);

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
            case R.id.bTest:
                startActivity(new Intent(this, test.class));
        }
    }
}
