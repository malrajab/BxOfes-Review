package com.example.m_alrajab.popularmovies.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.m_alrajab.popularmovies.BuildConfig;
import com.example.m_alrajab.popularmovies.R;

/**
 * Created by m_alrajab on 7/29/16.
 */
//No error handling at this stage
public class URLBuilderPref {
    private Context context;
    private String url;
    private String urlPoster;
    private  SharedPreferences sharedPref;
    public static final String QUERY_KEY_PREFIX="?api_key=";

    public URLBuilderPref(Context context) {
        this.context = context;
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


}
