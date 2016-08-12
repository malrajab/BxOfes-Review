package com.example.m_alrajab.popularmovies.controller.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by m_alrajab on 8/12/16.
 */

public class PopMoviesService extends IntentService {

    public static final String MOVIE_SORT_TYPE_EXTRA="MOVIE_SORT_TYPE_EXTRA";

    public PopMoviesService(String name) {
        super(name==null?"PopMovieService":name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }




    static public class AlarmReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
