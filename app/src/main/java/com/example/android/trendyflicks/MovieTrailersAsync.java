package com.example.android.trendyflicks;

import android.app.Activity;
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
import java.util.ArrayList;

/**
 * Created by Jerma_000 on 11/4/2015.
 */
public class MovieTrailersAsync extends AsyncTask<String, Void, String[]> {


    private final String LOG_TAG = MovieTrailersAsync.class.getSimpleName();

    private Activity activity;

   private static  ArrayList<String> mMovieTrailerLinks = new ArrayList<String>();


    public MovieTrailersAsync(){
        super();


    }


    public MovieTrailersAsync(Activity a) {

        this.activity = a;


    }


    public void addMovieTrailerLinks(String movieTrailerLinks){
        mMovieTrailerLinks.add(movieTrailerLinks);
    }
    public ArrayList<String> getMovieTrailerLinks(){
        return mMovieTrailerLinks;
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
      //  return new String[0];
        if (params.length == 0) {
            return null;
        }
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieTrailerJsonStr = null;
        final String key = "";
        try {

            final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/";
            final String API_KEY = "api_key";
            final String VIDEOS = "videos";

            Log.i(LOG_TAG, "Id parameter value " + params[0]);


            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(params[0]).appendPath(VIDEOS).appendQueryParameter(API_KEY, key).build();

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
            movieTrailerJsonStr = buffer.toString();
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

            return getMovieTrailerDataFromJson(movieTrailerJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the movies.
        Log.i(LOG_TAG, "Error getting or parsing the movies");
        return null;
    }

    private String[] getMovieTrailerDataFromJson(String movieTrailerJsonStr)
            throws JSONException {


        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_RESULTS = "results";

        final String TMDB_KEY = "key";
        final String TMDB_NAME = "name";

        JSONObject movieJsonObject = new JSONObject(movieTrailerJsonStr);
        JSONArray movieResultArray = movieJsonObject.getJSONArray(TMDB_RESULTS);

        String[] resultStrs = new String[movieResultArray.length()];

        MovieTrailerForDisplay trailersForDisplay = new MovieTrailerForDisplay();
        trailersForDisplay.clearMovieDetails();
        mMovieTrailerLinks.clear();
        Log.i(LOG_TAG, "movieTrailerArray " + movieResultArray);
        for(int i = 0; i < movieResultArray.length(); i++) {

            String trailerKey;
            String name;

             // Get the JSON object representing the trailer
            JSONObject movieTrailerInfo = movieResultArray.getJSONObject(i);

            trailerKey = movieTrailerInfo.getString(TMDB_KEY);
            name = movieTrailerInfo.getString(TMDB_NAME);





            addMovieTrailerLinks("vnd.youtube:" + trailerKey);

            resultStrs[i] = name;
            trailersForDisplay.addMovieDetailsForDisplay(name);
            Log.i(LOG_TAG, "movieTrailerResultStrs "+ resultStrs[i]);
        }
        return resultStrs;


    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param results The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String[] results) {
        if (results != null) {
     //       MovieTrailerForDisplay trailersForDisplay = new MovieTrailerForDisplay();
     //       trailersForDisplay.clearMovieDetails();
     /*       for(String trailerStr : results) {
                trailersForDisplay.addMovieDetailsForDisplay(trailerStr);
            }*/
        }
        Log.i(LOG_TAG, "In MovieTrailer on moviePostExecute ");
        // New data is back from the server.  Hooray!
    }

    public void print(){



        for (String movieTrailerLink : mMovieTrailerLinks) {
            Log.i(LOG_TAG,"Print Movie Trailer Link List item - " + movieTrailerLink);
        }
    }
}
