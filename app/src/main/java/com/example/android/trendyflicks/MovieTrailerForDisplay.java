package com.example.android.trendyflicks;

import java.util.ArrayList;

/**
 * Created by Jerma_000 on 11/19/2015.
 */
public class MovieTrailerForDisplay {
    private static ArrayList<String> mMovieTrailerListInfo = new ArrayList<String>();

    public  MovieTrailerForDisplay(){

    }

    public void addMovieDetailsForDisplay(String movieDetails){
        mMovieTrailerListInfo.add(movieDetails);
    }
    public ArrayList<String> getMovieTrailersForDisplay(){
        return mMovieTrailerListInfo;
    }
    public void clearMovieDetails(){
        mMovieTrailerListInfo.clear();
    }
}
