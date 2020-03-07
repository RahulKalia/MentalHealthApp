package com.example.fitnesstrackingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    TextView textView ;
    Button bRegister, bSettings, bStepCounter , bMoodPredict;
    ImageButton bVeryHappy, bHappy, bNeutral, bSad, bVerySad;
    DatabaseHelper mDatabaseHelper;
    ListView listView;
    BayesHelper bayesHelper;
    String todaysDate;

    // When activity is created create the local store and set button on click listener.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRegister = (Button) findViewById(R.id.bRegister);
        bSettings = (Button) findViewById(R.id.bSettings);
        bStepCounter = (Button) findViewById(R.id.bStepCounter);
        bMoodPredict = (Button) findViewById(R.id.bMoodPredict);

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
        bMoodPredict.setOnClickListener(this);
        bVeryHappy.setOnClickListener(this);
        bHappy.setOnClickListener(this);
        bNeutral.setOnClickListener(this);
        bSad.setOnClickListener(this);
        bVerySad.setOnClickListener(this);


        mDatabaseHelper = new DatabaseHelper(this);

        bayesHelper = new BayesHelper();
        bayesHelper.init();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        char[] time = timeNow.toString().toCharArray();
        todaysDate = dateNow.toString();
    }

    // On starting check if a user exists and if not redirect to the Register page.
    @Override
    protected void onStart(){
        super.onStart();
        if (authenticate() == true){
            displayUserDetails();
            bRegister.setVisibility(View.GONE);
            populateListView();
            mDatabaseHelper.populateDummyData();


            // Start the step counter service and set an alarm for it to be activated every hour to log the values it has collected
            AlarmManager scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getApplicationContext(), StepLoggerService.class );
            PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent accelerometerIntent = new Intent(getApplicationContext(), AccelerometerService.class );
            PendingIntent scheduledAccelIntent = PendingIntent.getService(getApplicationContext(), 0, accelerometerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            PendingIntent schdeuledNotificationIntent = PendingIntent.getBroadcast(this, 0,alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set alarm to interval of one mintue for testing purposes - normally AlarmManager.INTERVAL_HOUR
            long minute = 60 * 1000;
            scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minute, scheduledIntent);
            scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minute, scheduledAccelIntent);
            //Set every three hours
            scheduler.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR *3, schdeuledNotificationIntent);
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
            case R.id.bMoodPredict:
                startActivity(new Intent(this, MoodPredictor.class));
                break;
            case  R.id.bVeryHappy:
                saveMoodSelection(5);
                toastMessage("Thanks for logging your mood!");
                bayesHelper.updateMatrices(4,totalSteps(), totalJitters());
                break;
            case  R.id.bHappy:
                saveMoodSelection(4);
                toastMessage("Thanks for logging your mood!");
                bayesHelper.updateMatrices(3,totalSteps(),totalJitters());
                break;
            case  R.id.bNeutral:
                saveMoodSelection(3);
                toastMessage("Thanks for logging your mood!");
                bayesHelper.updateMatrices(2,totalSteps(),totalJitters());
                break;
            case  R.id.bSad:
                saveMoodSelection(2);
                toastMessage("Thanks for logging your mood!");
                bayesHelper.updateMatrices(1,totalSteps(),totalJitters());
                break;
            case  R.id.bVerySad:
                saveMoodSelection(1);
                toastMessage("Thanks for logging your mood!");
                bayesHelper.updateMatrices(0,totalSteps(),totalJitters());
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveMoodSelection(int moodSelection) {
        mDatabaseHelper.saveMood(moodSelection);
    }


    private void populateListView(){
        final ArrayList<layoutItem> listViewPopulate = new ArrayList<>();
        Cursor data = mDatabaseHelper.getListContents();
        if (data.getCount() == 0){
            toastMessage("The database is empty.");
        }else{
            while(data.moveToNext()){
                layoutItem item = new layoutItem(data.getString(0),data.getInt(1));

                listViewPopulate.add(item);

                final ItemListAdapter adapter = new ItemListAdapter(this, R.layout.adapter_view_layout, listViewPopulate);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, ChartDisplayer.class);
                        TextView textView = (TextView) view.findViewById(R.id.tvDate);
                        String s = listViewPopulate.get(position).getDate();
                        intent.putExtra("date", s);
                        startActivity(intent);

                    }
                });
            }
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public int totalSteps(){
        ArrayList<Integer> steps = mDatabaseHelper.getLastSteps(todaysDate);
        int totalSteps = 0;
        for (int i =0; i < steps.size(); i++){
            totalSteps += steps.get(i);
        }
        return totalSteps;
    }

    public int totalJitters(){
        ArrayList<Integer> steps = mDatabaseHelper.getJittersPerDay(todaysDate);
        int totalSteps = 0;
        for (int i =0; i < steps.size(); i++){
            totalSteps += steps.get(i);
        }
        return totalSteps;
    }


}
