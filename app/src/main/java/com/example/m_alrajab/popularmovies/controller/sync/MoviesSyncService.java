package com.example.m_alrajab.popularmovies.controller.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/**
 * Created by m_alrajab on 8/13/16.
 * THis class is part of the SyncAdapter framework
 * code has been used as it is from training provided by Udacity
 * - only modification is some name changes
 */

public class MoviesSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static MoviesSyncAdapter sMoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("MoviesSyncService", "onCreate - MoviesSyncService");
        synchronized (sSyncAdapterLock) {
            if (sMoviesSyncAdapter == null) {
                sMoviesSyncAdapter = new MoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMoviesSyncAdapter.getSyncAdapterBinder();
    }
}
