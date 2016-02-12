package com.example.android.trendyflicks;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jerma_000 on 1/24/2016.
 */
public class MovieKeyParcelable implements Parcelable {
    /**
     * Constructs a new instance of {@code Object}.
     */
    public MovieKeyParcelable() {
        super();
    }
    public MovieKeyParcelable(String movieDetails, String key, String mTwoPane) {
        this.movieDetails = movieDetails;
        this.key = key;
        this.mTwoPane = mTwoPane;
    }

    // parcel keys
    private static final String KEY_MOVIE_DETAILS = "movieDetails";
    private static final String KEY_MOVIE_KEY = "key";
    private static final String KEY_TWO_PANE = "mTwoPane";

    private String movieDetails;

    private String key;

    private String mTwoPane;


    public String getMovieDetails() {

        return movieDetails;

    }

    public void setMovieDetails(String movieDetails) {

        this.movieDetails = movieDetails;

    }


    public String getKey() {

        return key;

    }

    public void setKey(String key) {

        this.key = key;

    }

    public String getTwoPane() {

        return mTwoPane;

    }

    public void setTwoPane(String mTwoPane) {

        this.mTwoPane = mTwoPane;

    }

    public static final Parcelable.Creator<MovieKeyParcelable> CREATOR = new Creator<MovieKeyParcelable>() {

        public MovieKeyParcelable createFromParcel(Parcel source) {

       /*     MovieKeyParcelable movieKey = new MovieKeyParcelable();

            movieKey.movieDetails = source.readString();

            movieKey.key = source.readString();

            movieKey.mTwoPane = source.readString();*/
            Bundle bundle = source.readBundle();
           // return movieKey;
            // instantiate a person using values from the bundle
            return new MovieKeyParcelable(bundle.getString(KEY_MOVIE_DETAILS),
                    bundle.getString(KEY_MOVIE_KEY), bundle.getString(KEY_TWO_PANE));
        }

        public MovieKeyParcelable[] newArray(int size) {

            return new MovieKeyParcelable[size];

        }

    };


    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // create a bundle for the key value pairs
        Bundle bundle = new Bundle();

        bundle.putString(KEY_MOVIE_DETAILS ,movieDetails);

        bundle.putString(KEY_MOVIE_KEY,key);

        bundle.putString(KEY_TWO_PANE, mTwoPane);
        // write the key value pairs to the parcel
        parcel.writeBundle(bundle);

    }
}
