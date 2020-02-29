package com.example.fitnesstrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.ArrayList;



public class ChartDisplayer extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
    GraphView graph;
    TextView tvDetailedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_day);

        graph = (GraphView) findViewById(R.id.graph);
        tvDetailedDate = (TextView) findViewById(R.id.tvDetailedDate);
        // Getting intent date
        Intent intent = getIntent();
        final String date = intent.getStringExtra("date");
        tvDetailedDate.setText("You are viewing:  " + date);


        ArrayList<Integer> stepCount = mDatabaseHelper.getLastSteps(date);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, stepCount.get(0)),
                new DataPoint(2, stepCount.get(1)),
                new DataPoint(3, stepCount.get(2)),
                new DataPoint(4, stepCount.get(3)),
                new DataPoint(5, stepCount.get(4)),
                new DataPoint(6, stepCount.get(5))
        });
        graph.addSeries(series);
    }


}
