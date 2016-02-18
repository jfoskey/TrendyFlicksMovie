package com.example.android.trendyflicks;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by Jerma_000 on 10/13/2015.
 */
public class DownloadBitmapImages {
    private static LinkedHashMap<String ,Drawable> mMovieImageIdsList = new LinkedHashMap<String, Drawable>();
    private static LinkedHashMap<String ,String> mMoviePosterAndIdsList = new LinkedHashMap<String, String>();
    private ProgressDialog progressDialog;
    private Bitmap bitmap = null;
    private final String LOG_TAG = DownloadBitmapImages.class.getSimpleName();
    private final String imageURL = "http://image.tmdb.org/t/p/w500"  ;
    private Resources contextResources;

    public DownloadBitmapImages(Resources resources) {
        contextResources = resources;

    }

    public void setMoviePosterAndIds(String id, String posterImage){
        final String movieId = id;
        final String url = imageURL +  posterImage;
        mMoviePosterAndIdsList.put(movieId, url);
    }

    public void clearMoviePosterAndIds(){

        mMoviePosterAndIdsList.clear();

    }

    public LinkedHashMap<String , String> getMoviePosterAndIdsList(){
        return mMoviePosterAndIdsList;
    }

    public void  downloadImage(String id, String posterImage) {

        final String url = imageURL +  posterImage;
        final String movieId = id;
        Log.i(LOG_TAG, "url string  " + url);
        new Thread() {
            public void run() {
                InputStream in = null;


                try {
                    in = openHttpConnection(url);
                    bitmap = BitmapFactory.decodeStream(in);




                    // Use mContext.getResources if you are doing this in the Adapter
                    Drawable drawable = new BitmapDrawable(contextResources, bitmap);
                    Log.i(LOG_TAG, "drawable "+drawable);//+ movieId);
                    mMovieImageIdsList.put(movieId, drawable);

                    in.close();
                }

                catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }.start();
    }


    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public LinkedHashMap<String ,Drawable> getMovieIds(){
        return mMovieImageIdsList;
    }

    public void clearMovieIds(){
        mMovieImageIdsList.clear();
    }


    public void printMovieImageIdsQueue(){
        // Get a set of the entries
        Set set = mMovieImageIdsList.entrySet();
        Log.i(LOG_TAG, " mMovieImageIdsList size&&&&&&**** - " +  mMovieImageIdsList.size());
        // Get an iterator
       Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
            Log.i(LOG_TAG, "Key - " + me.getKey()+ ": value - " +me.getValue());
        }



      /*  for (Drawable movieImageIdsList : mMovieImageIdsList) {
            Log.i(LOG_TAG, "Drawable List item - " + movieImageIdsList);
        }*/
    }
}
