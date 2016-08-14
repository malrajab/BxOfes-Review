package com.example.m_alrajab.popularmovies.ux;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.sync.MoviesSyncAdapter;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
    public static final String EXTRA_NO_HEADERS = ":android:no_headers";
    static  final String TAG="Main activity";

   // FragmentManager fragmentManager;
    //FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragmentManager=getSupportFragmentManager();
       // fragmentTransaction=fragmentManager.beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            updateMovieList();
            File httpCacheDir = new File(this.getCacheDir(), "http");
            if(!httpCacheDir.toString().contains("youtube")){
            long httpCacheSize = 50 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);}
        } catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:" + e);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
    private void updateMovieList() {
        MoviesSyncAdapter.syncImmediately(this);
    }


}
