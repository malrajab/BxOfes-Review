package com.example.m_alrajab.popularmovies.controller.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by m_alrajab on 8/12/16.
 */

public class PopMoviesService extends IntentService {
    public PopMoviesService(String name) {
        super(name==null?"PopMovieService":name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
