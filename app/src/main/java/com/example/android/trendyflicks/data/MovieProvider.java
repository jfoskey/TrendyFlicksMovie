package com.example.android.trendyflicks.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Jerma_000 on 11/26/2015.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    //movie.sort_Type = ?
    //movie.sort_Type = ? AND date = ?
    //movieTrailer.movie_ID = ?
    //movie.movie_ID = ? AND movie.sort_Type = ?


    static final int MOVIE_INFO_SORT= 100;
    static final int MOVIE_INFO_SORT_WITH_DATE = 101;
    static final int MOVIE_TRAILER = 300;
    static final int MOVIE_INFO_ID_WITH_MOVIE_INFO_SORT= 102;


    private static final SQLiteQueryBuilder sMovieByIDQueryBuilder;

    static{
        sMovieByIDQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //movie INNER JOIN trailer ON Movietrailer._id = location._id
        sMovieByIDQueryBuilder.setTables(
                MovieContract.MovieInfoEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieTrailerEntry.TABLE_NAME +
                        " ON " +  MovieContract.MovieInfoEntry.TABLE_NAME +
                        "." + MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID  +
                        " = " + MovieContract.MovieTrailerEntry.TABLE_NAME +
                        "." + MovieContract.MovieTrailerEntry._ID );
    }


    //movie.sort_Type = ?
    private static final String sSortTypeSelection =
            MovieContract.MovieInfoEntry.TABLE_NAME+
                    "." + MovieContract.MovieInfoEntry.COLUMN_SORT_TYPE + " = ? ";

    //movie.sort_Type = ? AND date = ?
    private static final String sSortTypeWithStartDateSelection =
            MovieContract.MovieInfoEntry.TABLE_NAME+
                    "." + MovieContract.MovieInfoEntry.COLUMN_SORT_TYPE + " = ? AND " +
                    MovieContract.MovieInfoEntry.COLUMN_DATE + " >= ? ";

    //movieTrailer.movie_ID = ?
    private static final String sMovieTrailerelection =
            MovieContract.MovieInfoEntry.TABLE_NAME +
                    "." + MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + " = ? ";

    //movie.movie_ID = ? AND movie.sort_Type = ?
        private static final String sMovieAndSortTypeSelection =
            MovieContract.MovieInfoEntry.TABLE_NAME +
                    "." + MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + " = ? " +
                    "AND " +
                     MovieContract.MovieInfoEntry.COLUMN_SORT_TYPE + " = ?";

    //movie.sort_Type = ?
    //movie.sort_Type = ? AND date = ?
    private Cursor getMoviesBySortType(Uri uri,String[] projection,String sortOrder) {
        String sortType = MovieContract.MovieInfoEntry.getMovieIdFromUri(uri);
        long downLoadedDate = MovieContract.MovieInfoEntry.getColumnDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (downLoadedDate == 0) {
            selection = sSortTypeSelection;
            selectionArgs = new String[]{sortType};
        } else {
            selectionArgs = new String[]{sortType, Long.toString(downLoadedDate)};
            selection = sSortTypeWithStartDateSelection;
        }

        return sMovieByIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    //movie.movie_ID = ? and sort_Type = ?
    private Cursor getMovieUsingIdAndSortType(
            Uri uri, String[] projection, String sortOrder) {
        String movieID = MovieContract.MovieInfoEntry.getMovieIdFromUri(uri);
        String sortType = MovieContract.MovieInfoEntry.getSortTypeFromUri(uri);

        return sMovieByIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieAndSortTypeSelection,
                new String[]{movieID, sortType},
                null,
                null,
                sortOrder
        );
    }

    //movieTrailer.movie_ID = ?
    private Cursor getMovieTrailer(
            Uri uri, String[] projection, String sortOrder) {
        String movieID = MovieContract.MovieInfoEntry.getMovieIdFromUri(uri);

        return sMovieByIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieTrailerelection ,
                new String[]{movieID},
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.

        //movie.sort_Type = ?
        //movie.sort_Type = ? AND date = ?
        //movieTrailer.movie_ID = ?
        //movie.movie_ID = ? AND movie.sort_Type = ?
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE_INFO_SORT);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_INFO_ID_WITH_MOVIE_INFO_SORT);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*/#", MOVIE_INFO_SORT_WITH_DATE);

        matcher.addURI(authority, MovieContract.PATH_TRAILER, MOVIE_TRAILER);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.
     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE_INFO_SORT:
                return MovieContract.MovieInfoEntry.CONTENT_TYPE;
            case MOVIE_INFO_SORT_WITH_DATE:
                return MovieContract.MovieInfoEntry.CONTENT_TYPE;
            case MOVIE_INFO_ID_WITH_MOVIE_INFO_SORT:
                return MovieContract.MovieInfoEntry.CONTENT_ITEM_TYPE;

            case MOVIE_TRAILER:
                return MovieContract.MovieInfoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.

        //movie.sort_Type = ?
        //movie.sort_Type = ? AND date = ?
        //movieTrailer.movie_ID = ?
        //movie.movie_ID = ? AND movie.sort_Type = ?
        //matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE_INFO_SORT);
        //matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_INFO_ID_WITH_MOVIE_INFO_SORT);
        //matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*/#", MOVIE_INFO_SORT_WITH_DATE);
        //matcher.addURI(authority, MovieContract.PATH_TRAILER, MOVIE_TRAILER);
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "Movie/*/*"
            case MOVIE_INFO_SORT_WITH_DATE:
            {
                retCursor = getMoviesBySortType(uri, projection, sortOrder);
                break;
            }
            // "Movie/*"
            case MOVIE_INFO_ID_WITH_MOVIE_INFO_SORT:
            {
                retCursor = getMovieUsingIdAndSortType(uri, projection, sortOrder);
                break;
            }
            // "Movie"
            case MOVIE_INFO_SORT: {
                // TODO: 12/9/2015
        /*        retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );*/
                retCursor = getMoviesBySortType(uri, projection, sortOrder);
                break;
            }
            // "Trailer"
            case MOVIE_TRAILER: {
                // TODO: 12/9/2015
   /*             retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );*/
                retCursor = getMovieTrailer(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE_INFO_SORT: {
                normalizeDate(values);
                long _id = db.insert(MovieContract.MovieInfoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieInfoEntry.buildMovieInfoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_TRAILER: {
                long _id = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieTrailerEntry.buildMovieTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE_INFO_SORT:
                rowsDeleted = db.delete(
                        MovieContract.MovieTrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_TRAILER:
                rowsDeleted = db.delete(
                        MovieContract.MovieTrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(MovieContract.MovieInfoEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(MovieContract.MovieInfoEntry.COLUMN_DATE);
            values.put(MovieContract.MovieInfoEntry.COLUMN_DATE, MovieContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE_INFO_SORT:
                normalizeDate(values);
                rowsUpdated = db.update(MovieContract.MovieInfoEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_TRAILER:
                rowsUpdated = db.update(MovieContract.MovieTrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case  MOVIE_INFO_SORT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(MovieContract.MovieInfoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}

