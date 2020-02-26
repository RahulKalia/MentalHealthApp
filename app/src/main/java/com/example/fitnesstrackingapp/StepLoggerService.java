package com.example.fitnesstrackingapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class StepLoggerService extends Service implements SensorEventListener {

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private static final String TAG = "StepLoggerService";
    DatabaseHelper mDatabaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand started");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mDatabaseHelper = new DatabaseHelper(this);

        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();
        
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get values and timestamp from main thread
        new SensorEventLoggerTask().execute(event);

        Log.d(TAG,"Current value inside: " +event.values[0]);

        // stop the service
        stopSelf();
    }

    private class SensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            // log the value
            addSteps(event.values[0], event.timestamp);
            return null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addSteps(float steps, long timestamp){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        char[] time = timeNow.toString().toCharArray();
        String dt = dateNow.toString();

        // Parsing char array to quarter calculator
        int qt = mDatabaseHelper.calculateQuarterID(time);

        Log.d(TAG,"Adding new record: " + dt +" " + qt +" " + steps +" "+ 1);
        mDatabaseHelper.addStepCounterData(dt, qt, steps, 1);
        
    }
}
