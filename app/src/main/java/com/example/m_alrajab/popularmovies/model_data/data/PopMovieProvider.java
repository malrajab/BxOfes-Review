package com.example.m_alrajab.popularmovies.model_data.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemReviewEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemTrailerEntry;

import static com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.CONTENT_AUTHORITY;
import static com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.PATH_MOVIE;
import static com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.PATH_MOVIE_REVIEW;
import static com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.PATH_MOVIE_TRAILER;


/**
 * Created by m_alrajab on 8/3/16.
 * Not fully implemented:
 * ToDo:
 * ToDo 1:- Better SE is required to enhance the design of this content provider
 * ToDo 2:- Upcoming movies, and now playing not 100% accurate.
 * ToDo 3:- Joining tables need a rethink
 * ToDo 4:- favorite DB implementation instead of SharedPreference.
 */
@SuppressWarnings("NullableProblems")
public class PopMovieProvider extends ContentProvider {
    private PopMovieDbHelper mOpenHelper;
    static final int MOVIE_ITEM         = 100;
    static final int MOVIE_POPULAR      = 201;// featured
    static final String SORT_MOVIE_POP      = MovieItemEntry.COLUMN_MOVIE_POPULARITY+" DESC";
    static final int MOVIE_TOP_RATED    = 202;// featured
    static final String SORT_MOVIE_TOPRATED = MovieItemEntry.COLUMN_MOVIE_RATING+" DESC";
    static final int MOVIE_UPCOMING     = 203;//this feature is not implemented - in this code
    static final int MOVIE_NOW_PLAYING  = 204;//this feature is not implemented - in this code
    // static final String SORT_MOVIE_NOWPLAYING = MovieItemEntry.COLUMN_MOVIE_RELEASE+" DESC";
    static final int MOVIE_FAVORIATE    = 205;//this feature is not implemented using db
    static final int MOVIE_WITH_REVIEW  = 300;// featured
    static final int MOVIE_WITH_TRAILER = 400;// featured
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final SQLiteQueryBuilder sMovieReviewsQueryBuilder;
    static{ // join the review and movie entry tables
        sMovieReviewsQueryBuilder = new SQLiteQueryBuilder();
        sMovieReviewsQueryBuilder.setTables(  MovieItemReviewEntry.TABLE_NAME + " INNER JOIN " +
                MovieItemEntry.TABLE_NAME + " ON " + MovieItemReviewEntry.TABLE_NAME +
                "." + MovieItemReviewEntry.COLUMN_REVIEW_OF_MOVIE_KEY +
                " = " + MovieItemEntry.TABLE_NAME + "." + MovieItemEntry._ID);      }

    private static final SQLiteQueryBuilder sMovieTrailersQueryBuilder;
    static{ // join the trailers and movie entry tables
        sMovieTrailersQueryBuilder = new SQLiteQueryBuilder();
        sMovieTrailersQueryBuilder.setTables(  MovieItemTrailerEntry.TABLE_NAME + " INNER JOIN " +
                MovieItemEntry.TABLE_NAME + " ON " + MovieItemTrailerEntry.TABLE_NAME +
                "." + MovieItemTrailerEntry.COLUMN_TRAILER_OF_MOVIE_KEY +
                " = " + MovieItemEntry.TABLE_NAME + "." + MovieItemEntry._ID);      }

    // movie/popular.sort_popular = ?
    private static final String sMovieItemSelection =
            MovieItemEntry.TABLE_NAME+
                    "." + MovieItemEntry.COLUMN_MOVIE_ID + " = ? ";
    /**
     ToDo 5: check the way upcoming and nowplaing is presented
     // movie/popular.sort_popular = ?
     private static final String sPopularMoviesSelection =
     MovieItemEntry.TABLE_NAME+
     "." + MovieItemEntry.COLUMN_MOVIE_SORT_POP + " = ? ";
     // movie/popular.sort_top_rated = ?
     private static final String sTopRatedMoviesSelection =
     MovieItemEntry.TABLE_NAME+
     "." + MovieItemEntry.COLUMN_MOVIE_SORT_TOP_RATE + " = ? ";
     // movie/popular.sort_favorite = ?
     private static final String sFavoriteMoviesSelection =
     MovieItemEntry.TABLE_NAME+
     "." + MovieItemEntry.COLUMN_MOVIE_SORT_FAV + " = ? ";
     // movie/popular.sort_upcoming = ?
     private static final String sUpComingMoviesSelection =
     MovieItemEntry.TABLE_NAME+
     "." + MovieItemEntry.COLUMN_MOVIE_SORT_UPCOMING + " = ? ";
     // movie/popular.sort_now_plaing = ?
     private static final String sNowPlayingMoviesSelection =
     MovieItemEntry.TABLE_NAME+
     "." + MovieItemEntry.COLUMN_MOVIE_SORT_NOW_PLAYING + " = ? ";
     */

    private static final String sMovieReviewsSelection =
            MovieItemEntry.TABLE_NAME+
                    "." + MovieItemEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMovieTrailersSelection =
            MovieItemEntry.TABLE_NAME+
                    "." + MovieItemEntry.COLUMN_MOVIE_ID + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopMovieDbHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE+"/popular"       ,MOVIE_POPULAR);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE+"/top_rated"     ,MOVIE_TOP_RATED);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE+"/upcoming"      ,MOVIE_UPCOMING);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE+"/now_playing"   ,MOVIE_NOW_PLAYING);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE+"/favorite/"     ,MOVIE_FAVORIATE);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE_REVIEW+"/#/reviews"     ,MOVIE_WITH_REVIEW);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE_TRAILER+"/#/videos"      ,MOVIE_WITH_TRAILER);
        return matcher;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //use matcher to determine type of uri
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_FAVORIATE:
            case MOVIE_UPCOMING:
            case MOVIE_NOW_PLAYING:
            case MOVIE_TOP_RATED:
            case MOVIE_POPULAR:
                return MovieItemEntry.CONTENT_TYPE;
            case MOVIE_WITH_REVIEW:
                return MovieItemReviewEntry.CONTENT_TYPE;
            case MOVIE_WITH_TRAILER:
                return MovieItemTrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI type: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        Uri retUri;
        long id;

        switch (match) {
            case MOVIE_FAVORIATE:
            case MOVIE_POPULAR:
            case MOVIE_UPCOMING:
            case MOVIE_TOP_RATED:
            case MOVIE_NOW_PLAYING:
                id = db.insertOrThrow(MovieItemEntry.TABLE_NAME, null, values);
                if (id > 0) retUri = MovieItemEntry.buildMovieUri(id);
                else throw new android.database.SQLException("Insert failed. URI: " + uri);
                break;
            case MOVIE_WITH_REVIEW:
                id = db.insertWithOnConflict(MovieItemReviewEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) retUri = MovieItemReviewEntry.buildMovieUri(id);
                else throw new android.database.SQLException("Insert failed for review URI: " + uri);
                break;
            case MOVIE_WITH_TRAILER:
                id = db.insertWithOnConflict(MovieItemTrailerEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) retUri = MovieItemTrailerEntry.buildMovieUri(id);
                else throw new android.database.SQLException("Insert failed for trailer URI: " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI type: " + uri);
        }
       // if (getContext() != null){        }
            getContext().getContentResolver().notifyChange(uri, null);

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        selection= (selection == null)?"1":selection;
        switch (match) {
            case MOVIE_FAVORIATE:
            case MOVIE_POPULAR:
            case MOVIE_UPCOMING:
            case MOVIE_TOP_RATED:
            case MOVIE_NOW_PLAYING:
                rowsDeleted = db.delete(MovieItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_WITH_REVIEW:
                rowsDeleted = db.delete(MovieItemReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_WITH_TRAILER:
                rowsDeleted = db.delete(MovieItemTrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI type: " + uri);
        }
        if (getContext() != null){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_TOP_RATED:case MOVIE_NOW_PLAYING:case MOVIE_UPCOMING:// "movie/top_rated"
                retCursor = mOpenHelper.getReadableDatabase().query(MovieItemEntry.TABLE_NAME,
                        projection,selection, selectionArgs, null, null, SORT_MOVIE_TOPRATED);
                break;
            case MOVIE_POPULAR: // "movie/popular"
                retCursor = mOpenHelper.getReadableDatabase().query(MovieItemEntry.TABLE_NAME,
                        projection,selection, selectionArgs, null, null, SORT_MOVIE_POP);
                break;
/**
 *ToDo 6: check toDo5
 case MOVIE_UPCOMING:// "movie/upcoming"
 retCursor = mOpenHelper.getReadableDatabase().query(MovieItemEntry.TABLE_NAME,
 projection,selection, selectionArgs, null, null, null);
 break;
 case MOVIE_NOW_PLAYING:// "movie/now_playing"
 retCursor = mOpenHelper.getReadableDatabase().query(MovieItemEntry.TABLE_NAME,
 projection,selection, selectionArgs, null, null, SORT_MOVIE_NOWPLAYING);
 break;
 case MOVIE_FAVORIATE: // "movie/favorite"
 retCursor = mOpenHelper.getReadableDatabase().query(MovieItemEntry.TABLE_NAME,
 projection,selection, selectionArgs, null, null, null);
 break;
 */
            case MOVIE_WITH_REVIEW: // "movie/#/reviews"
                retCursor =  mOpenHelper.getReadableDatabase().query(MovieItemReviewEntry.TABLE_NAME,
                        projection,selection, selectionArgs, null, null, null);
                break;
            case MOVIE_WITH_TRAILER: // "movie/#/videos"
                retCursor =  mOpenHelper.getReadableDatabase().query(MovieItemTrailerEntry.TABLE_NAME,
                        projection,selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOVIE_FAVORIATE:
            case MOVIE_POPULAR:
            case MOVIE_UPCOMING:
            case MOVIE_TOP_RATED:
            case MOVIE_NOW_PLAYING:
                rowsUpdated = db.update(MovieItemEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            case MOVIE_WITH_REVIEW:
                rowsUpdated = db.update(MovieItemReviewEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            case MOVIE_WITH_TRAILER:
                rowsUpdated = db.update(MovieItemTrailerEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI type: " + uri);
        }
        if (getContext() != null){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);int returnCount;
        switch (match) {
            case MOVIE_POPULAR:
            case MOVIE_TOP_RATED:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieItemEntry.TABLE_NAME, null, value);
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
            case MOVIE_WITH_REVIEW:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieItemReviewEntry.TABLE_NAME, null, value);
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
            case MOVIE_WITH_TRAILER:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieItemTrailerEntry.TABLE_NAME, null, value);
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

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
