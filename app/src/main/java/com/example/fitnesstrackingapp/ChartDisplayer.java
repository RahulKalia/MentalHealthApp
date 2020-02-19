package com.example.fitnesstrackingapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.ArrayList;



public class ChartDisplayer extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartdisplayer);

        graph = (GraphView) findViewById(R.id.graph);
        ArrayList<Integer> stepCount = mDatabaseHelper.getLastSteps();

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
