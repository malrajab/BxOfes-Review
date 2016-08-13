package com.example.m_alrajab.popularmovies.controller.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * Created by m_alrajab on 8/13/16.
 * Required for the sync frame work. Even though now accounts is related to this app
 */

public class MoviesAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private MoviesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MoviesAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
