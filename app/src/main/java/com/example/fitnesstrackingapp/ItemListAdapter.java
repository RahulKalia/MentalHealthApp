package com.example.fitnesstrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<layoutItem> {

    private static final String TAG = "ItemListAdapter";

    private Context context;
    private int resource;
    private int lastPosition = -1;

    // Holds variables in a view
    static class ViewHolder{
        TextView date;
        TextView mood;
    }

    public ItemListAdapter(Context context, int resource, ArrayList<layoutItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the item information
        String date = getItem(position).getDate();
        int mood = getItem(position).getMood();
        String moodString = mood + "";

        // Create the item object with the information
        layoutItem item = new layoutItem(date,mood);

        final View result;
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById((R.id.textView1));
            holder.mood = (TextView) convertView.findViewById(R.id.textView2);

            result = convertView;

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        result.startAnimation(animation);
        lastPosition = position;

        holder.date.setText(date);
        holder.mood.setText(moodString);

        return convertView;
    }










}
