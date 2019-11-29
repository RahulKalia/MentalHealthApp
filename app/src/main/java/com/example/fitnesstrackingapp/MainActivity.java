package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    Button bRegister;
    UserLocalStore localStore;

    // When activity is created create the local store and set button on click listener.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);

        localStore = new UserLocalStore(this);
    }

    // On starting check if a user exists and if not redirect to the Register page.
    @Override
    protected void onStart(){
        super.onStart();
        if (userExists() == true){
            displayUserDetails();
        }else{
            startActivity(new Intent(this, Register.class));
        }
    }

    // Method to check userExists
    private  boolean userExists(){
        return localStore.checkUserConfigured();
    }

    // Method to display User Details - only used if user has been authenticated to exist.
    // TO BE CHANGED - additional features to be added here.
    private void displayUserDetails(){
        User currentUser = localStore.getUser();
        etName.setText(currentUser.name);
    }

    // This changes the view to the register class - TO BE CHANGED TO SOMETHING USEFUL LATER.
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}
