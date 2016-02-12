package com.example.android.trendyflicks.data;

/**
 * Created by Jerma_000 on 11/26/2015.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.trendyflicks";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.trendy/movie/ is a valid path for
    // looking at weather data. content://com.example.android.trendyflicks.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the location table */
    public static final class  MovieTrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // Table name
        public static final String TABLE_NAME = "trailers";

        // The mpvie id string is what will be sent to theMovieDB
        // as the trailer query.
        public static final String COLUMN_MOVIE_ID = "movie_id";



        public static final String COLUMN_TRAILER_LINK = "trailer_link";


        public static final String COLUMN_TRAILER_NAME= "trailer_name";



        public static Uri buildMovieTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class MovieInfoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // Column with the foreign key into the location table.
        public static final String COLUMN_MOVIE_ID = "movie_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_SORT_TYPE= "sort_type";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_POSTER= "poster";
        public static final String COLUMN_OVERVIEW  = "overview";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_USER_RATING = "user_rating";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // Windspeed is stored as a float representing windspeed  mph
        public static final String COLUMN_TITLE= "title";

        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
       // public static final Bitmap COLUMN_BITMAP;// = "bitmap";

        public static final String COLUMN_FAVORITE = "favorite";




        public static Uri buildMovieInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*
            This is the buildMovieTrailer function you filled in.
         */

        //movieTrailer.movie_ID = ?
        public static Uri buildMovieTrailer(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        //movie.sort_Type = ? AND date = ?
        public static Uri buildMovieWithSortTypeAndDate(
                String sortType, long date) {
            long normalizedDate = normalizeDate(date);
            return CONTENT_URI.buildUpon().appendPath(sortType)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
        }
        //movie.movie_ID = ? AND movie.sort_Type = ?
        public static Uri buildMovieWithSortType(String movieId, String sortType) {
            return CONTENT_URI.buildUpon().appendPath(movieId)
                    .appendPath(sortType).build();
        }

        //movie.sort_Type = ?
        public static Uri buildMovieWithSortType(String sortType) {
            return CONTENT_URI.buildUpon().appendPath(sortType).build();
        }
        
        public static long getColumnDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(10));
        }

        public static String getSortTypeFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
      /*  public static Uri buildWeatherLocationWithStartDate(
                String locationSetting, long startDate) {
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }*/
    }

}
