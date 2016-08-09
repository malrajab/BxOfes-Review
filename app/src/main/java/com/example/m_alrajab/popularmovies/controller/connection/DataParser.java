package com.example.m_alrajab.popularmovies.controller.connection;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.model_data.MovieItem;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemReviewEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemTrailerEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by m_alrajab on 7/28/16.
 */
public class DataParser {
    private SharedPreferences sharedPref;
    private final String LOG_TAG = this.getClass().toString();
    private Context mContext;
    private String pathURL;
    private String data;
    private URLBuilderPref urlBuilder;
    private String[] parsingParameters;

    private ArrayList<MovieItem> movieItemArrayList=new ArrayList<>();

    public DataParser(Context context, String urlPath, String... parsingParameters) {
        this.mContext = context;
        this.pathURL = urlPath;
        this.parsingParameters=parsingParameters;
        urlBuilder=new URLBuilderPref(mContext);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }



    @Nullable
    public ArrayList<MovieItem> parseData(String... params){
        params=parsingParameters;
        APIConnection conn=new APIConnection(mContext,pathURL);
        try {
            data = conn.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try{
            if(data==null)
                return null;
            JSONObject mJson =new JSONObject(data);
            JSONArray moviesArray = mJson.getJSONArray(params[0]);

            for(int i = 0; i < moviesArray.length(); i++) {
                MovieItem item=new MovieItem();
                JSONObject singleMovie = moviesArray.getJSONObject(i);
                item.setId(singleMovie.getInt(params[1]));
                item.setTitle(singleMovie.getString(params[2]));
                item.setOverview(singleMovie.getString(params[3]));
                item.setPopularity((float)singleMovie.getDouble(params[4]));
                item.setUserRating((float)singleMovie.getDouble(params[5]));
                item.setReleaseDate(Date.valueOf(singleMovie.getString(params[6])));
                item.setPosterImagePath(singleMovie.getString(params[7]));
                item.setBackdropPath(singleMovie.getString(params[8]));
                addMovieItemToDB(item);
                movieItemArrayList.add(item);
            }
            new Thread(new Runnable(){@Override public void run(){addReviewItemToDB();}}).start();
            new Thread(new Runnable(){@Override public void run(){addTrailerItemToDB();}}).start();
            return movieItemArrayList;
        } catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG,e.getMessage(),e);
        }

        return null;
    }

    private void addReviewItemToDB(){
        ContentValues values; int currentMovieID;
        APIConnection conn;
        for(MovieItem movieItem:movieItemArrayList){
            data=null;
            currentMovieID=movieItem.getId();
            try {
                conn=new APIConnection(mContext,urlBuilder.getReviewApiURL(currentMovieID));
                data = conn.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try{
                if(data!=null) {
                    JSONObject mJson = new JSONObject(data);
                    JSONArray reviewsArray = mJson.getJSONArray("results");
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        values = new ContentValues();
                        JSONObject singlReview = reviewsArray.getJSONObject(i);
                        values.put(MovieItemReviewEntry.COLUMN_REVIEW_OF_MOVIE_KEY, currentMovieID);
                        values.put(MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_ID,
                                singlReview.getString(MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_ID));
                        values.put(MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR,
                                singlReview.getString(MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR));
                        values.put(MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT,
                                singlReview.getString(MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT));
                        mContext.getContentResolver().insert(
                                MovieItemReviewEntry.CONTENT_URI.buildUpon().appendEncodedPath(""+currentMovieID+"/reviews").build(),
                                values);
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
                Log.e(LOG_TAG,e.getMessage(),e);
            }

        }

    }

    private void addTrailerItemToDB(){
        ContentValues values; int currentMovieID;
        APIConnection conn;
        for(MovieItem movieItem:movieItemArrayList){
            data=null;
            currentMovieID=movieItem.getId();
            Log.v(LOG_TAG +"Trailer>>>>>>>>",urlBuilder.getTrailerApiURL(currentMovieID));
            try {
                conn=new APIConnection(mContext,urlBuilder.getTrailerApiURL(currentMovieID));
                data = conn.execute().get();
                Log.v("\n \n Trailer>>Data>>>"+currentMovieID,data);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                if(data!=null) {
                    JSONObject mJson = new JSONObject(data);
                    JSONArray reviewsArray = mJson.getJSONArray("results");
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        values = new ContentValues();
                        JSONObject singlTrailer = reviewsArray.getJSONObject(i);
                        values.put(MovieItemTrailerEntry.COLUMN_TRAILER_OF_MOVIE_KEY, currentMovieID);
                        values.put(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_ID,
                                singlTrailer.getString(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_ID));
                        values.put(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_KEY,
                                singlTrailer.getString(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_KEY));
                        values.put(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_NAME,
                                singlTrailer.getString(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_NAME));
                        values.put(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_SITE,
                                singlTrailer.getString(MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_SITE));
                        mContext.getContentResolver().insert(
                                MovieItemTrailerEntry.CONTENT_URI.buildUpon().appendEncodedPath(""+currentMovieID+"/videos").build(),
                                values);
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
                Log.e(LOG_TAG,e.getMessage(),e);
            }
        }
    }


    private long addMovieItemToDB(MovieItem item){
        String sorting_pref_temp=sharedPref.getString(
                mContext.getString(R.string.pref_sorting_key),"top_rated");
        Cursor cursor= mContext.getContentResolver().query(
                MovieItemEntry.CONTENT_URI.buildUpon().appendPath(sorting_pref_temp).build()
                ,
                new String[]{MovieItemEntry._ID},
                MovieItemEntry.COLUMN_MOVIE_ID +" = ? ",
                new String[]{Integer.toString(item.getId())},
                null
        );
        Log.v(LOG_TAG+">>>"+
                Integer.toString(item.getId()),MovieItemEntry.CONTENT_URI.buildUpon().appendPath(sharedPref.getString(
                mContext.getString(R.string.pref_sorting_key),"/top_rated")).build().toString());
       // if(cursor==null) {
       //     return 0;
       // }
        long idIndex;
        if(cursor.moveToFirst() )
            idIndex=cursor.getLong(cursor.getColumnIndex(MovieItemEntry._ID));
        else{
            ContentValues movieItemValues = new ContentValues();
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_ID, item.getId());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_TITLE, item.getTitle());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_OVERVIEW, item.getOverview());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_POPULARITY, item.getPopularity());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_RATING, item.getUserRating());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_RELEASE, item.getReleaseDate().toString());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_POSTERPATH, item.getPosterImagePath());
            movieItemValues.put(MovieItemEntry.COLUMN_MOVIE_BACKDROPPATH, item.getBackdropPath());
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieItemEntry.CONTENT_URI.buildUpon().appendPath(sorting_pref_temp).build(),
                    movieItemValues
            );
            idIndex = ContentUris.parseId(insertedUri);
        }

        cursor.close();
        return idIndex;
    }


}
