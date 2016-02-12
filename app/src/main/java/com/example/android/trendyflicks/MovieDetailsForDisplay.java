package com.example.android.trendyflicks;

import java.util.LinkedHashMap;

/**
 * Created by Jerma_000 on 10/29/2015.
 */
public class MovieDetailsForDisplay {
    private static LinkedHashMap<String, String> mMovieListInfo = new LinkedHashMap<String, String>();

    public MovieDetailsForDisplay(){

    }

    public void addMovieDetailsForDisplay(String id, String movieDetails){
        mMovieListInfo.put(id, movieDetails);
    }
    public LinkedHashMap<String, String> getMovieDetailsForDisplay(){
        return mMovieListInfo;
    }
    public void clearMovieDetails(){
        mMovieListInfo.clear();
    }
}
