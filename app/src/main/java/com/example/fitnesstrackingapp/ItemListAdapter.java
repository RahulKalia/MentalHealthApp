package com.example.fitnesstrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<layoutItem> {

    private static final String TAG = "ItemListAdapter";

    private Context context;
    private int resource;
    private int lastPosition = -1;

    // Holds variables in a view
    private static class ViewHolder{
        TextView date;
        ImageView img;
    }

    public ItemListAdapter(Context context, int resource, ArrayList<layoutItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        setupImageLoader();

        // Get the item information
        String date = getItem(position).getDate();
        int mood = getItem(position).getMood();
        String imgUrl = getImgURL(mood);

        // Create the item object with the information
        //layoutItem item = new layoutItem(date,mood);

        final View result;
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById((R.id.textView1));

            //initialize image view
            holder.img = (ImageView) convertView.findViewById(R.id.imageView1);

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

        int defaultImage =context.getResources().getIdentifier("@drawable/image_failed",null, context.getPackageName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(imgUrl, holder.img, options);

        holder.date.setText(date);


        return convertView;
    }

    private String getImgURL(int mood) {
        switch(mood) {
            case 1:
                return "drawable://" + R.drawable.ic_very_sad;
            case 2:
                return "drawable://" + R.drawable.ic_sad;
            case 3:
                return "drawable://" + R.drawable.ic_neutral;
            case 4:
                return "drawable://" + R.drawable.ic_happy;
            case 5:
                return "drawable://" + R.drawable.ic_very_happy;
            default:
                throw new IllegalStateException("Unexpected value: " + mood);
        }
    }

    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    private String getItemDate(ViewHolder holder){
        String date = holder.date.toString();
        return date;
    }




}
