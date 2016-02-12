package com.example.android.trendyflicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String MOVIEFRAGMENT_TAG = "MFTAG";
    private String mMovieSortOrder;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    public static boolean mTwoPane = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieSortOrder = Utility.getPreferredMovieSortOrder(this);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "IN on Create Method");

        Log.i(LOG_TAG, "move_detal_container not equal Null?" + (findViewById(R.id.movie_detail_container) != null) );



        if ( findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.

            mTwoPane = true;
            Log.i(LOG_TAG, "mTwopane is " + mTwoPane);
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailActivity.DetailFragment(), MOVIEFRAGMENT_TAG)
                        .commit();
            }


        } else {
            mTwoPane = false;
            Log.i(LOG_TAG, "mTwopane is " + mTwoPane);
        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.i(LOG_TAG, "IN onCreateOptionsMenu Method");
        return true;

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
        Log.i(LOG_TAG, "IN onOptionsItemSelected Method");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "IN onStop Method");
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        Log.i(LOG_TAG, "IN  onPostResume Method");
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "IN onDestroy Method");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "IN onPause Method");
        super.onPause();
    }

    @Override
    protected void onStart(){
        Log.i(LOG_TAG, "IN onStart Method");
        super.onStart();

    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "IN onResume Method");
        super.onResume();


        String movieSortOrder = Utility.getPreferredMovieSortOrder(this );
        // update the movie in our second pane using the fragment manager
        if (movieSortOrder != null && !movieSortOrder.equals(mMovieSortOrder)) {
            Log.i(LOG_TAG, "Sort order changed from " + mMovieSortOrder+ " to "+ movieSortOrder );
           MainActivityMovieFragment mamf = (MainActivityMovieFragment)getSupportFragmentManager().findFragmentById(R.id.container);

            if ( null != mamf  ) {
                Log.i(LOG_TAG, "Calling movieSortOrder  " );
                mamf.onSortChanged();
            }
            mMovieSortOrder = movieSortOrder;
        }
    }



}
