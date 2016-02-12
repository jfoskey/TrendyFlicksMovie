package com.example.android.trendyflicks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Jerma_000 on 10/11/2015.
 */
public class MovieImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Drawable> mMovieImageIds;
    private final String LOG_TAG = MovieImageAdapter.class.getSimpleName();

    // Constructor
    public MovieImageAdapter(Context c, ArrayList<Drawable> list) {

        mContext = c;
        mMovieImageIds = list;
    }

    public int getCount() {
        return mMovieImageIds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
        //return mMovieImageIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
        //return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setLayoutParams(new GridView.LayoutParams(500, 700));
            imageView.setLayoutParams(new GridView.LayoutParams(300, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        }
        else
        {
            imageView = (ImageView) convertView;
        }
        Log.i(LOG_TAG, "**position** " + position);
        imageView.setImageDrawable(mMovieImageIds.get(position));
        return imageView;
    }



}
