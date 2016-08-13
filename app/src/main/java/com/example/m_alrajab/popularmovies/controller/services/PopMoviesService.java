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
    private final String LOG_TAG = PopMoviesService.class.getSimpleName();
    private static Context mContext;

    public PopMoviesService() {
        super("PopMovieService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String sortValue=intent.getStringExtra(PopMoviesService.MOVIE_SORT_TYPE_EXTRA);
        new DataParser(mContext,sortValue).parseData();
    }

    static public class AlarmReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext=context;
            Intent sendIntent = new Intent(context, PopMoviesService.class);
            sendIntent.putExtra(PopMoviesService.MOVIE_SORT_TYPE_EXTRA,
                    intent.getStringExtra(PopMoviesService.MOVIE_SORT_TYPE_EXTRA));
            context.startService(sendIntent);
        }
    }
}
