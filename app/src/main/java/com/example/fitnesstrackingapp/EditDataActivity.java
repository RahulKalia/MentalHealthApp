package com.example.fitnesstrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditDataActivity";

    Button bSaveChange,bDelete;
        EditText etItem;

    DatabaseHelper mDatabaseHelper;

    private String selectedName;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        bSaveChange = (Button) findViewById(R.id.bSaveChange);
        bDelete = (Button) findViewById(R.id.bDelete);
        etItem = (EditText) findViewById(R.id.etItem);
        mDatabaseHelper = new DatabaseHelper(this);

        // Get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        // Get the itemID extra
        selectedID = receivedIntent.getIntExtra("id", -1); // -1 is default

        // Get the name extra
        selectedName = receivedIntent.getStringExtra("name");

        // Set the text to show the current selected name
        etItem.setText(selectedName);

        bSaveChange.setOnClickListener(this);

        bDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSaveChange:
                String item = etItem.getText().toString();
                if(!item.equals("")){
                    //mDatabaseHelper.updateName(item,selectedID,selectedName);
                    toastMessage("Success!");
                }else{
                    toastMessage("You must enter a name");
                }
                break;
            case R.id.bDelete:
                mDatabaseHelper.deleteName(selectedID,selectedName);
                etItem.setText("");
                toastMessage("Item has been removed");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
