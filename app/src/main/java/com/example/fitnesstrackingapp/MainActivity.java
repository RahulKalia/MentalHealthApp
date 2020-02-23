package com.example.fitnesstrackingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView ;
    Button bRegister, bSettings, bStepCounter , bTest;
    ImageButton bVeryHappy, bHappy, bNeutral, bSad, bVerySad;
    DatabaseHelper mDatabaseHelper;
    ListView listView;


    // When activity is created create the local store and set button on click listener.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRegister = (Button) findViewById(R.id.bRegister);
        bSettings = (Button) findViewById(R.id.bSettings);
        bStepCounter = (Button) findViewById(R.id.bStepCounter);
        bTest = (Button) findViewById(R.id.bTest);

        bVeryHappy = (ImageButton) findViewById(R.id.bVeryHappy);
        bHappy = (ImageButton) findViewById(R.id.bHappy);
        bNeutral = (ImageButton) findViewById(R.id.bNeutral);
        bSad = (ImageButton) findViewById(R.id.bSad);
        bVerySad = (ImageButton) findViewById(R.id.bVerySad);

        textView = (TextView) findViewById(R.id.textView);

        listView = (ListView) findViewById(R.id.listView);

        bSettings.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        bStepCounter.setOnClickListener(this);
        bTest.setOnClickListener(this);
        bVeryHappy.setOnClickListener(this);
        bHappy.setOnClickListener(this);
        bNeutral.setOnClickListener(this);
        bSad.setOnClickListener(this);
        bVerySad.setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelper(this);
    }

    // On starting check if a user exists and if not redirect to the Register page.
    @Override
    protected void onStart(){
        super.onStart();
        if (authenticate() == true){
            displayUserDetails();
            bRegister.setVisibility(View.GONE);
            populateListView();

            // Start the step counter service and set an alarm for it to be activated every hour
            AlarmManager scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), StepLoggerService.class );
            Intent savingIntent = new Intent(getApplicationContext(), StepLoggerService.class );
            PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set alarm to interval of one mintue for testing purposes - normally AlarmManager.INTERVAL_HOUR
            long minute = 60 * 1000;
            scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minute, scheduledIntent);

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

    // Method to display Details - only used if user has been authenticated to exist.
    // TO BE CHANGED - additional features to be added here.
    private void displayUserDetails(){
        String name = mDatabaseHelper.getColumn("name");
        textView.setText("Hi " + name + "!");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                startActivity(new Intent(this, ChartDisplayer.class));
                break;
            case  R.id.bVeryHappy:
                saveMoodSelection(5);
                break;
            case  R.id.bHappy:
                saveMoodSelection(4);
                break;
            case  R.id.bNeutral:
                saveMoodSelection(3);
                break;
            case  R.id.bSad:
                saveMoodSelection(2);
                break;
            case  R.id.bVerySad:
                saveMoodSelection(1);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveMoodSelection(int moodSelection) {
        mDatabaseHelper.saveMood(moodSelection);
    }


   private void populateListView(){
        ArrayList<layoutItem> listViewPopulate = new ArrayList<>();
        Cursor data = mDatabaseHelper.getListContents();
        if (data.getCount() == 0){
            toastMessage("The database is empty.");
        }else{
            while(data.moveToNext()){
                layoutItem item = new layoutItem(data.getString(0),data.getInt(1));

                listViewPopulate.add(item);

                ItemListAdapter adapter = new ItemListAdapter(this, R.layout.adapter_view_layout, listViewPopulate);
                listView.setAdapter(adapter);
            }
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
