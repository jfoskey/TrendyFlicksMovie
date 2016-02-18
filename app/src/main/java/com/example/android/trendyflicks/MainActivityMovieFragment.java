package com.example.android.trendyflicks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityMovieFragment extends Fragment {
    private final String LOG_TAG = MainActivityMovieFragment.class.getSimpleName();
    private static final String MAINACTIVITYMOVIEFRAGMENT_TAG = "MAMFTAG";
    public  final static String PAR_KEY = "com.example.android.trendyflicks.MovieKeyParcelable.par";
    private static String typeOfString;

   // private static LinkedHashMap<String ,Drawable> mFavoriteMovieList = new LinkedHashMap<String, Drawable>();

    public static LinkedHashMap<String ,String> mMovieDetailsList = new LinkedHashMap< String,String>();

    private static LinkedHashMap<String,String> mFavoriteList= new LinkedHashMap<String, String>();

    public static LinkedHashMap<String ,String> mMovieDetailImage = new LinkedHashMap<String,String>();

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    
    public  MainActivityMovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.

        Log.i(LOG_TAG, "IN onCreate Method");
        setHasOptionsMenu(true);

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.




        Log.i(LOG_TAG, "IN onCreateView Method");

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        //GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);


        //retrieves the linkedHashMap containing the movieID and the poster
        DownloadBitmapImages imagesPosterAndIdList = new DownloadBitmapImages(getActivity().getResources());



           new  AsyncImageTask().execute();
           mProgressBar.setVisibility(View.VISIBLE);




        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String movieSortOrder = Utility.getPreferredMovieSortOrder(getActivity());
                String key;
                ArrayList<String> movieKey;
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                if (movieSortOrder != null && movieSortOrder.equals("favorite")) {
                 //   ArrayList<String> favoriteMovieListKeys = new ArrayList<String>(mFavoriteMovieList.keySet());
                    Log.i(LOG_TAG, "mFavoriteList click size - " + mFavoriteList.size() );
                    ArrayList<String>  favoriteMovieKey = new ArrayList<String>( mFavoriteList.keySet());
                 key = favoriteMovieKey.get(position);
                    Log.i(LOG_TAG, "favorite click key - " + key );
                } else{

                DownloadBitmapImages imageItemId = new DownloadBitmapImages(getActivity().getResources());
                movieKey = new ArrayList<String>(imageItemId.getMoviePosterAndIdsList().keySet());
                key = movieKey.get(position);
                }

                 String movieDetails = new MovieDetailsForDisplay().getMovieDetailsForDisplay().get(key);


                if(MainActivity.mTwoPane ){

                    mMovieDetailsList.clear();
                    mMovieDetailImage.clear();
                    mMovieDetailsList.put(key, movieDetails);

                    mMovieDetailImage.put(key,item.getImage());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, new MovieDetailActivity.DetailFragment(), MAINACTIVITYMOVIEFRAGMENT_TAG)
                            .commit();

                }else { // go to separate activity
                    // launch detail activity using intent
                    mMovieDetailsList.clear();
                    mMovieDetailsList.put(key,movieDetails);

                    Intent i = new Intent(getActivity(), MovieDetailActivity.class);
                    i.putExtra("movieDetails", movieDetails);
                    i.putExtra("image", item.getImage());
                    i.putExtra("key",key);
                    startActivity(i);
                }

            }
        });

        return rootView;
    }



    void onSortChanged( ) {
        Log.i(LOG_TAG, " updating Movies " );
        updateMovies();

    }

    private void updateMovies() {
        FetchMovieInfoUpdate MovieUpdate = new FetchMovieInfoUpdate(getResources(), getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default));
        Log.i(LOG_TAG, " sort Order " + sortOrder);

        if (!sortOrder.equals("favorite")){
            Log.i(LOG_TAG, "the sort change is not favorite!!!!!!!" );
            MovieUpdate.execute(sortOrder);
        } else{
            Log.i(LOG_TAG, "the Preference was Favorite sending intent to  MainActivityMovieFragment" );
            Intent i = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(i);
            // close this activity
            getActivity().finish();
        }

    }


    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onStop() {
        Log.i(LOG_TAG, "IN onStop Method");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "IN onDestroy Method");
        super.onDestroy();
    }

    @Override
    public  void onPause() {
        Log.i(LOG_TAG, "IN onPause Method");
        super.onPause();
    }


    @Override
    public  void onResume() {
        Log.i(LOG_TAG, "IN onResume Method");
        super.onResume();
    }

    //Downloading data asynchronously
    public class AsyncImageTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            GridItem item;
            Integer result = 0;


            DownloadBitmapImages poster = new DownloadBitmapImages(getActivity().getResources());
            String movieSortOrder = Utility.getPreferredMovieSortOrder(getActivity());


            // update the movie in our second pane using the fragment manager
            Log.i(LOG_TAG, "Async movieSortOrder " + movieSortOrder);
            if (movieSortOrder != null && movieSortOrder.equals("favorite")) {
                SharedPreferences sp = getActivity().getSharedPreferences(Integer.toString(R.string.pref_favorite_movies), Context.MODE_PRIVATE);
                Log.i(LOG_TAG, "Async Favorite");
                //print sharedPref
                Map<String, ?> all = sp.getAll();
                Log.i(LOG_TAG, "all size " + all.size());
                String key, value;
                mFavoriteList.clear();
                for (Map.Entry<String, ?> entry : all.entrySet()) {
                    key = entry.getKey();
                    Log.i(LOG_TAG, "KEY " + key);
             /*   if(entry.getValue() instanceof String) {
                    value = (String) entry.getValue();
                    Log.i(LOG_TAG, "KEY " + key + "value " + value );
                    // encrypt and store again

                }*/
                    if (poster.getMoviePosterAndIdsList().containsKey(key)) {
                        Log.i(LOG_TAG, " adding KEY " + key);
                        mFavoriteList.put(key, poster.getMoviePosterAndIdsList().get(key));
                    }
                }
                Log.i(LOG_TAG, "Favorite List Size - " + mFavoriteList.size());
                if (!mFavoriteList.isEmpty()) {
                    for (String url : mFavoriteList.values()) {

                        item = new GridItem();
                        item.setImage(url);
                        Log.i(LOG_TAG, "Favorite Image URL" + item.getImage());
                        mGridData.add(item);
                    }
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            }else {

                if (!poster.getMoviePosterAndIdsList().isEmpty()) {
                    for (String url : poster.getMoviePosterAndIdsList().values()) {

                        item = new GridItem();
                        item.setImage(url);
                        Log.i(LOG_TAG, "Image URL" + item.getImage());
                        mGridData.add(item);
                    }
                    result = 1; // Successful
                } else {
                    updateMovies();
                    result = 0; //"Failed
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result== 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
