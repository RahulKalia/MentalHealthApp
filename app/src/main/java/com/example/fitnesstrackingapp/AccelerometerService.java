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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class AccelerometerService extends Service implements SensorEventListener {


    private static final String TAG = "AccelerometerService";
    DatabaseHelper mDatabaseHelper;
    int count = 1;
    private boolean init;
    private Sensor mySensor;
    private SensorManager SM;
    private float x1, x2, x3;
    private static final float ERROR = (float) 7.0;
    private static final float SHAKE_THRESHOLD = 11.00f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000000;
    private long mLastShakeTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand started");

        //Toast.makeText(this, "Start Detecting", Toast.LENGTH_LONG).show();
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        mDatabaseHelper = new DatabaseHelper(this);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                Log.d("mySensor", "Acceleration is " + acceleration + "m/s^2");

                if (acceleration > SHAKE_THRESHOLD) {
                        mLastShakeTime = curTime;
                    Toast.makeText(getApplicationContext(), "SHAKE DETECTED",
                            Toast.LENGTH_LONG).show();

                    // get values and timestamp from main thread
                    new SensorEventLoggerTask().execute(event);
                }
            }
        }

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
            addShake(event.values[0], event.timestamp);
            return null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addShake(float shake, long timestamp){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        char[] time = timeNow.toString().toCharArray();
        String dt = dateNow.toString();

        // Parsing char array to quarter calculator
        int qt = mDatabaseHelper.calculateQuarterID(time);

        Log.d(TAG,"Adding new record: " + dt +" " + qt +" " + shake +" "+ 1);
        mDatabaseHelper.addShakeDetectorData(dt, qt, 1);

    }
}
