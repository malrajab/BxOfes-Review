package com.example.m_alrajab.popularmovies.controller.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import com.example.m_alrajab.popularmovies.BuildConfig;
import com.example.m_alrajab.popularmovies.R;

/**
 * Created by m_alrajab on 7/29/16.
 */
//No error handling at this stage
public class URLBuilderPref implements Parcelable{

    private Context context;
    private String url;
    private String urlPoster;
    private  SharedPreferences sharedPref;
    public static final String QUERY_KEY_PREFIX="?api_key=";

    public URLBuilderPref(Context context) {
        this.context = context;
    }

    protected URLBuilderPref(Parcel in) {
        url=in.readString();
    }

    public static final Creator<URLBuilderPref> CREATOR = new Creator<URLBuilderPref>() {
        @Override
        public URLBuilderPref createFromParcel(Parcel in) {
            return new URLBuilderPref(in);
        }

        @Override
        public URLBuilderPref[] newArray(int size) {
            return new URLBuilderPref[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    public String getAPIURL(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String syncConnPref =sharedPref.getString(context.getString(R.string.pref_sorting_key), "top_rated");
        return context.getString(R.string.api_base_url)+syncConnPref+
                QUERY_KEY_PREFIX+ BuildConfig.POP_MOVIES_APIKEY;
    }

    public String getReviewApiURL(int id){
        return context.getString(R.string.api_base_url) + id + "/reviews" + QUERY_KEY_PREFIX+
                BuildConfig.POP_MOVIES_APIKEY;
    }

    public String getTrailerApiURL(int id){
        return context.getString(R.string.api_base_url) + id + "/videos" + QUERY_KEY_PREFIX+
                BuildConfig.POP_MOVIES_APIKEY;
    }

    public String getPosterApiBaseURL(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String syncConnPref =sharedPref.getString(context.getString(R.string.pref_poster_res_key), "/w500");
        return context.getString(R.string.poster_base_url)+ syncConnPref;
    }

    // re-engineering
//    public int getPosterWidth(){
//
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        String syncConnPref =sharedPref.getString(context.getString(R.string.pref_poster_res_key), "/w500");
//        //No error handling at this stage
//        int wdth=Integer.parseInt(syncConnPref.split("w")[1]);
//        return wdth;
//    }


}
