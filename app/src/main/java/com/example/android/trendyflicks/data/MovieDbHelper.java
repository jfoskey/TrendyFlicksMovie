package com.example.android.trendyflicks.data;

/**
 * Created by Jerma_000 on 11/26/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.trendyflicks.data.MovieContract.MovieInfoEntry;
import com.example.android.trendyflicks.data.MovieContract.MovieTrailerEntry;
/**
 * Manages a local database for weather data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
     /*   final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " +  LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY," +
                LocationEntry.COLUMN_LOCATION_SETTING + " TEXT UNIQUE NOT NULL, " +
                LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG + " REAL NOT NULL " +
                " );";*/
        final String SQL_CREATE_MOVIE_TRAILER_TABLE = "CREATE TABLE " + MovieTrailerEntry.TABLE_NAME + " (" +

                MovieTrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieTrailerEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieTrailerEntry.COLUMN_TRAILER_LINK + " TEXT NOT NULL, " +
                MovieTrailerEntry.COLUMN_TRAILER_NAME + " REAL NOT NULL, " +
                " );";

        final String SQL_CREATE_MOVIE_INFO_TABLE = "CREATE TABLE " + MovieInfoEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MovieInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data

                MovieInfoEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieInfoEntry.COLUMN_SORT_TYPE + "  TEXT NOT NULL, " +
                MovieInfoEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieInfoEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieInfoEntry.COLUMN_USER_RATING + " TEXT NOT NULL," +

                MovieInfoEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieInfoEntry.COLUMN_TITLE + " TEXT NOT NULL, " +

               // MovieInfoEntry.COLUMN_BITMAP + " BLOB NOT NULL, " +
                MovieInfoEntry.COLUMN_FAVORITE + " INTEGER  NOT NULL, " +
                MovieInfoEntry.COLUMN_DATE  + " INTEGER NOT NULL, " +

                // Set up the movie column as a foreign key to Movie Trailer table.
                " FOREIGN KEY (" + MovieInfoEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieTrailerEntry.TABLE_NAME + " (" +  MovieTrailerEntry.COLUMN_MOVIE_ID   + "), " +

                // To assure the application have just one Movie entry per day
                // per Sort type, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MovieInfoEntry.COLUMN_DATE + ", " +
                MovieInfoEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieTrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  MovieInfoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
