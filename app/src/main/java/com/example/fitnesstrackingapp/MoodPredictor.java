package com.example.fitnesstrackingapp;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MoodPredictor extends AppCompatActivity {

    ImageView ivMoodResult;
    BayesHelper bayesHelper = new BayesHelper();
    DatabaseHelper databaseHelper = new DatabaseHelper(this);



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_predictor);


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        char[] time = timeNow.toString().toCharArray();
        String dt = dateNow.toString();

        ArrayList<Integer> steps = databaseHelper.getLastSteps(dt);
        int totalSteps = 0;
        for (int i =0; i < steps.size(); i++){
            totalSteps += steps.get(i);
        }


        int mood = bayesHelper.getMood(totalSteps);

        ivMoodResult = (ImageView) findViewById(R.id.imageView);
        switch (mood){
            case 1:
                ivMoodResult.setImageResource(R.drawable.ic_very_sad);
                break;
            case 2:
                ivMoodResult.setImageResource(R.drawable.ic_sad);
                break;
            case 3:
                ivMoodResult.setImageResource(R.drawable.ic_neutral);
                break;
            case 4:
                ivMoodResult.setImageResource(R.drawable.ic_happy);
                break;
            case 5:
                ivMoodResult.setImageResource(R.drawable.ic_very_happy);
                break;
    }


    }



}
