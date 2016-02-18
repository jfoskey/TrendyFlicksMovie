package com.example.android.trendyflicks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jerma_000 on 10/28/2015.
 */
public class SplashScreen extends Activity {
    private final String LOG_TAG = SplashScreen.class.getSimpleName();
    //private ArrayList<String> mMovieListInfo = new ArrayList<String>();
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      /*  new Handler().postDelayed(new Runnable() { */

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

       /*     @Override
           public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);*/
        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default));
        /**
         * Async Task to make http call
         */
        new FetchMovieInfo().execute(sortOrder);
    }

    public class FetchMovieInfo extends AsyncTask<String, Void, String[]> {

            private final String LOG_TAG = FetchMovieInfo.class.getSimpleName();
    private Resources contextResources;
    // private final Context mContext;
    public Activity activity;
    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */
    public FetchMovieInfo() {
        super();
    }

    public FetchMovieInfo(Resources resources, Activity a ) {
        contextResources = resources;
        this.activity = a;
    }


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String[] doInBackground(String... params) {
        // TODO: 10/11/2015 get movie data then call getMovieDataFromJson
        // If there's no sort order, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
        final String key = "";
        try {
            // Construct the URL for the theMovieDb query
            //http://api.themoviedb.org/3/movie/popular?api_key=
            //http://api.themoviedb.org/3/movie/top_rated?api_key=
            // http://api.themoviedb.org/3/discover/movie?api_key=    &sort_by=popularity.desc
            //http://api.themoviedb.org/3/discover/movie?api_key=    &sort_by=vote_average.desc
            //final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/";
            //final String QUERY_PARAM = "q";
            // TODO: 11/3/2015 Change to this and test
            final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/discover/movie";
            final String API_KEY = "api_key";
            final String SORT_BY = "sort_by";
            final String CERTIFICATION_COUNTRY = "certification_country";
            Log.i(LOG_TAG, "sort parameter value " + params[0]);

            //Get the shared preference sort order
//                SharedPreferences sharedPrefs =
//                        PreferenceManager.getDefaultSharedPreferences(getActivity());
//                String sortOrder = sharedPrefs.getString(
//                        getString(R.string.pref_sort_order_key),
//                        getString(R.string.pref_sort_order_popular));

            //Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(params[0]).appendPath("?"+API_KEY).appendPath("="+key).build();
            // .appendQueryParameter(API_KEY, key).build();  certification_country=US

            //Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(params[0]).appendQueryParameter(API_KEY,key).build();

            // TODO: 11/3/2015 change and test this
            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(API_KEY, key).appendQueryParameter(SORT_BY, params[0] + ".desc").appendQueryParameter(CERTIFICATION_COUNTRY, "US").build();

            //.appendPath("?"+API_KEY).appendPath("="+key).build();
            // .appendQueryParameter(API_KEY, key).build();


            Log.i(LOG_TAG, "Built URI "+ builtUri.toString());
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                Log.i(LOG_TAG, "Input Stream is NULL ");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            // TODO: 10/12/2015 add  getWeatherDataFromJson
            return getMovieDataFromJson(movieJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the movies.
        Log.i(LOG_TAG, "Error getting or parsing the movies");
        return null;
    }

    private String[] getMovieDataFromJson(String movieJsonStr)
            throws JSONException {


        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_ID = "id";
        final String TMDB_TITLE = "title";


        JSONObject movieJsonObject = new JSONObject(movieJsonStr);
        JSONArray movieResultArray = movieJsonObject.getJSONArray(TMDB_RESULTS);

        String[] resultStrs = new String[movieResultArray.length()];
        DownloadBitmapImages downloadImage = new DownloadBitmapImages(contextResources);
        downloadImage.clearMovieIds();
        MovieDetailsForDisplay detailsForDisplay = new MovieDetailsForDisplay();
        detailsForDisplay.clearMovieDetails();
        Log.i(LOG_TAG, "movieResultArray " + movieResultArray);
        ArrayList<String> movieTralierList = new ArrayList<String>();
        for(int i = 0; i < movieResultArray.length(); i++) {

            String poster;
            String overview;
            String userRating;
            String releaseDate;
            String id;
            String title;

            // Get the JSON object representing the movie
            JSONObject movieInfo = movieResultArray.getJSONObject(i);


            poster = movieInfo.getString(TMDB_POSTER_PATH);
            overview = movieInfo.getString(TMDB_OVERVIEW);
            userRating = movieInfo.getString(TMDB_VOTE_AVERAGE);
            releaseDate = movieInfo.getString(TMDB_RELEASE_DATE);
            id = movieInfo.getString(TMDB_ID);
            title = movieInfo.getString(TMDB_TITLE);

            downloadImage.downloadImage( id,poster);


            resultStrs[i] = id + ";" + poster + ";" + overview + ";" + userRating + ";" + releaseDate+";" + title;
            Log.i(LOG_TAG, "resultStrs " + resultStrs[i]);

            detailsForDisplay.addMovieDetailsForDisplay(id, resultStrs[i]);

            movieTralierList.add(id);
        }
        Log.i(LOG_TAG, "movieTralierList Size " + movieTralierList.size());
        MovieTrailersAsync2 movieTrailers = new MovieTrailersAsync2();
        movieTrailers.execute(movieTralierList);
        //Log.i(LOG_TAG, "movieTrailers Linked Hash Set size in Splash Screen " + movieTrailers.mMovieTrailers.size());
        downloadImage.printMovieImageIdsQueue();
        return resultStrs;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // before making http calls

    }


    @Override
    public void onPostExecute(String[] result) {

        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }



}



}
