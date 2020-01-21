package com.example.fitnesstrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class test extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "test";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(this);
        btnViewData.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(newEntry);
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
                break;

            case R.id.btnView:
                Intent intent = new Intent(test.this, ListDataActivity.class);
                startActivity(intent);
        }
    }

    public void AddData(String newEntry) {
        /*boolean insertData = mDatabaseHelper.addRegisterData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }*/
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

