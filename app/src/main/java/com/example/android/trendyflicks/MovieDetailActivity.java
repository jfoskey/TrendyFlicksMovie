package com.example.android.trendyflicks;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Jerma_000 on 10/22/2015.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get intent data
        Intent i = getIntent();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new DetailFragment())
                    .commit();
        }




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private android.widget.ArrayAdapter<String> mTrailer;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }


        private static final String MyPREFERENCES = "com.example.android.trendyflicks.PREFERENCE_FAVORITE_MOVIES" ;
        public static ArrayList<String> mMovieTrailerLinks = new ArrayList<String>();


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail_view, container, false);


            String newString = null;
            int position;
            String key = null;


            Log.i(LOG_TAG, "TwoPane  " + MainActivity.mTwoPane);
                if (MainActivity.mTwoPane) {
                    Log.i(LOG_TAG, "MainActivityMovieFragment.mMovieDetailsList " + MainActivityMovieFragment.mMovieDetailsList);
                    if(!MainActivityMovieFragment.mMovieDetailsList.isEmpty()) {

                        MainActivityMovieFragment.mMovieDetailsList.entrySet().iterator().next();
                        newString = MainActivityMovieFragment.mMovieDetailsList.entrySet().iterator().next().getValue();
                        key = MainActivityMovieFragment.mMovieDetailsList.entrySet().iterator().next().getKey();
                        Log.i(LOG_TAG, "mMovieDetailsList newString" + newString);
                        Log.i(LOG_TAG, "mMovieDetailsList key" + key);
                    }
                }else {

                    Intent intent = getActivity().getIntent();
                    if (savedInstanceState == null) {
                        Bundle extras = intent.getExtras();
                        if (extras == null) {
                            newString = null;
                            position = 0;
                            key = null;
                        } else {
                            newString = extras.getString("movieDetails");
                            position = extras.getInt("position");
                            key = extras.getString("key");
                        }
                    } else {
                        newString = (String) savedInstanceState.getSerializable("movieDetails");
                        position = (int) savedInstanceState.getSerializable("position");
                        key = (String) savedInstanceState.getSerializable("key");
                    }
                }



            //get image
            // id + poster + " - " + overview + " - " + userRating + " - " + releaseDate ;
            Log.i(LOG_TAG, "newString " + newString);
            String movieDetails = newString;

          if(movieDetails != null){

            String[] parts = movieDetails.split(";");
            String id = parts[0]; // id
            String poster = parts[1]; // poster
            String overView = parts[2]; // overview
            String userRating = parts[3]; //userRating
            String releaseDate = parts[4]; //releaseDate
            String title = parts[5]; //title

            Log.i(LOG_TAG, "movieDetails to display in the details view " + " id- " + id + " poster- " + poster +
                    " overview- " + overView + " userRating- " + userRating + " releaseDate- " + releaseDate);
            //Log.i(LOG_TAG, "position in the details view " + "- " + position);

            DownloadBitmapImages image = new DownloadBitmapImages(this.getResources());

            Log.i(LOG_TAG, "movieImageIdsQueue SIZE " + image.getMovieIds().size());

            ImageView imageView = (ImageView) rootView.findViewById(R.id.SingleView);

            android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
           // layoutParams.width = 500;
           // layoutParams.height = 700;
            //  layoutParams.width = 300;
            //  layoutParams.height = 500;
              layoutParams.width = 200;
              layoutParams.height = 400;

            imageView.setLayoutParams(layoutParams);

            Log.i(LOG_TAG, "id VALUE " + id);
            //Log.i(LOG_TAG, "imageDrawable " + image.getMovieIds().get(position));


            imageView.setImageDrawable(image.getMovieIds().get(key));

            //set text
            TextView txtMovieID, txtOverview, txtUserRating, txtReleaseDate, txtTitle,txtTrailerLabel, txtOverviewLabel;



            //txtMovieID = (TextView) rootView.findViewById(R.id.movieID);
            txtOverview = (TextView) rootView.findViewById(R.id.overview);
            txtUserRating = (TextView) rootView.findViewById(R.id.userRating);
            txtReleaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
            txtTitle = (TextView) rootView.findViewById(R.id.title);
            txtTrailerLabel = (TextView)rootView.findViewById(R.id.trailersText);
            txtOverviewLabel = (TextView)rootView.findViewById(R.id.overviewText);

              // Diplaying the text
            //txtMovieID.setText("Movie # " + id);
            txtOverview.setText(overView);
            txtUserRating.setText("User Rating: " + userRating);
            txtReleaseDate.setText("Release Date: " + releaseDate);
            txtTitle.setText("Title: " + title);
            txtTrailerLabel.setText("Trailers (scroll)" );
            txtOverviewLabel.setText("Overview ");



            // Get a reference to the ListView, and attach this adapter to it.

            MovieTrailersAsync2 movieTrailerForDisplay = new MovieTrailersAsync2();
              movieTrailerForDisplay.getTrailersName(key);


              mMovieTrailerLinks.clear();
              mMovieTrailerLinks = movieTrailerForDisplay.getTrailerLinks(key);
              Log.i(LOG_TAG, "movieDetailActivity mMovieTrailerLinks - size " + mMovieTrailerLinks );
              Log.i(LOG_TAG, "movieDetailActivity mMovieTrailerName - size " + movieTrailerForDisplay.getTrailersName(key).size());

            ArrayList trailerNames = movieTrailerForDisplay.getTrailersName(key);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_movie_trailers, R.id.list_item_movie_trailers_textview, trailerNames);


            ListView listView = (ListView) rootView.findViewById(R.id.trailers);


            listView.setAdapter(adapter);
            listView.getCount();

            Log.i(LOG_TAG, "adapter count " +  listView.getCount()  );

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    String youTubeTrailer = mMovieTrailerLinks.get(position);
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youTubeTrailer));
                        String id = youTubeTrailer.substring(youTubeTrailer.indexOf(":")+1,youTubeTrailer.length());
                        Log.i(LOG_TAG, "movieDetailActivity trailer ID - " + id);
                        startActivity(intent);
                    }catch (ActivityNotFoundException ex){
                        String id = youTubeTrailer.substring(youTubeTrailer.indexOf(":")+1,youTubeTrailer.length());
                        Intent intent=new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v="+id));
                        startActivity(intent);
                    }
                }
            });

            // favorite Button
            //Create button dynamically
            ViewGroup linearLayout = (ViewGroup) rootView.findViewById(R.id.movieDetail);


            CheckBox chk = new CheckBox(this.getActivity(), null, android.R.attr.starStyle);
            chk.setId(Integer.parseInt(id)); //set movieID as the button ID
              chk.setText("Favorite");
              chk.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                      ViewGroup.LayoutParams.WRAP_CONTENT));

           linearLayout.addView(chk);


            final CheckBox favorite = (CheckBox) rootView.findViewById(Integer.parseInt(id));

            if (favorite.isChecked()) Log.i(LOG_TAG, "YES IT IS!!!! checked");
            favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                SharedPreferences sp = getActivity().getSharedPreferences(Integer.toString(R.string.pref_favorite_movies), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();


                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //do stuff

                    if (isChecked) {
                        Log.i(LOG_TAG, "Favorite is checked");
                        favorite.getId();
                        editor.putString(Integer.toString(favorite.getId()), Integer.toString(favorite.getId()));

                    } else {
                        Log.i(LOG_TAG, "Favorite is unChecked");

                        editor.remove(Integer.toString(favorite.getId()));
                    }
                    editor.apply();
                    //retrieve shared preference
                    String test = sp.getString(Integer.toString(favorite.getId()), "NOT working BOOOOOO!");
                    Log.i(LOG_TAG, "TEST ******** " + test);


                    //print sharedPref
                    Map<String, ?> all = sp.getAll();
                    String key, value;
                    for (Map.Entry<String, ?> entry : all.entrySet()) {
                        key = entry.getKey();
                        if (entry.getValue() instanceof String) {
                            value = (String) entry.getValue();
                            Log.i(LOG_TAG, "KEY " + key + "value " + value);
                            // encrypt and store again
                        }
                    }
                }
            });
        }//split conditional
            return rootView;
        }




    }

}
