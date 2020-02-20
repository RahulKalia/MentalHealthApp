package com.example.fitnesstrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class TwoColumn_ListAdapter extends ArrayAdapter<MoodItem> {

    private LayoutInflater mInflater;
    private ArrayList<MoodItem> moodItems;
    private int mViewResourceId;

    public TwoColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<MoodItem> moodItems){
        super(context, textViewResourceId, moodItems);
        this.moodItems  = moodItems;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        MoodItem moodItem = moodItems.get(position);

        if (moodItem != null){
            TextView date = (TextView) convertView.findViewById(R.id.tvDate);
            TextView mood = (TextView) convertView.findViewById(R.id.tvMood);

            if (date != null){
                date.setText(moodItem.getDate());
            }

            if (mood != null && moodItem.getMood() > 0){
                int moodInt = moodItem.getMood();
                mood.setText(Integer.toString(moodInt));
            }
        }
        return convertView;
    }


}
