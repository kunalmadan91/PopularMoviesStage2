package com.example.android.popularmoviesstage1.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.R;

import java.util.ArrayList;




public class VideoAdapter extends ArrayAdapter<String>{

    private Context context = getContext();

    public VideoAdapter(Context context, ArrayList<String> resource) {
        super(context,0, resource);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        String keys = getItem(position);


        final String key = getItem(position);
        Log.v("position","position>>>"+position+" "+key);

        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.video_image, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);


             Log.v("keyy","keyy>>>"+key);
        imageView.setImageResource(R.drawable.play1);

        return convertView;
    }
}
