package com.example.android.trendyflicks;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
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

/**
 * Created by Jerma_000 on 12/28/2015.
 */
public class FetchMovieInfoUpdate extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchMovieInfoUpdate.class.getSimpleName();
    private Resources contextResources;
    // private final Context mContext;
    public Activity activity;
    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */
    public FetchMovieInfoUpdate() {
        super();
    }

    public FetchMovieInfoUpdate(Resources resources, Activity a ) {
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
// TODO: 10/12/2015 add movie preferences

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
        downloadImage.clearMoviePosterAndIds();
        Log.i(LOG_TAG, "movieResultArray " + movieResultArray);
        for(int i = 0; i < movieResultArray.length(); i++) {

            String poster;
            String overview;
            String userRating;
            String releaseDate;
            String id;
            String title;

            // Get the JSON object representing the day
            JSONObject movieInfo = movieResultArray.getJSONObject(i);

            // description is in a child array called "weather", which is 1 element long.
            //  JSONObject weatherObject = movieInfo.getJSONArray(OWM_WEATHER).getJSONObject(0);
            poster = movieInfo.getString(TMDB_POSTER_PATH);
            overview = movieInfo.getString(TMDB_OVERVIEW);
            userRating = movieInfo.getString(TMDB_VOTE_AVERAGE);
            releaseDate = movieInfo.getString(TMDB_RELEASE_DATE);
            id = movieInfo.getString(TMDB_ID);
            title = movieInfo.getString(TMDB_TITLE);
            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.


            //downloadImage.downloadImage( id,poster);
            downloadImage.setMoviePosterAndIds(id,poster);

            resultStrs[i] = id + ";" + poster + ";" + overview + ";" + userRating + ";" + releaseDate+";" + title;
            Log.i(LOG_TAG, "resultStrs " + resultStrs[i]);
            // if (resultStrs.length != 0) {

            // for(String moviesStr : result) {
            detailsForDisplay.addMovieDetailsForDisplay(id, resultStrs[i]);
            // }
            // New data is back from the server.  Hooray!
            // }
        }
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
    /*        if (result != null) {
                MovieDetailsForDisplay detailsForDisplay = new MovieDetailsForDisplay();
                detailsForDisplay.clearMovieDetails();
                for(String moviesStr : result) {
                    detailsForDisplay.addMovieDetailsForDisplay(moviesStr);
                }
                // New data is back from the server.  Hooray!
            }*/
        // After completing http call
        // will close this activity and lauch main activity
        Log.i(LOG_TAG, "actiivity" + activity);
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
        // close this activity
        activity.finish();
        Log.i(LOG_TAG, "You are in the update PostExecute");
    }
}
