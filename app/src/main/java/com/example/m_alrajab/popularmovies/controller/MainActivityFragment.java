package com.example.m_alrajab.popularmovies.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.m_alrajab.popularmovies.BuildConfig;
import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.connection.DataParser;
import com.example.m_alrajab.popularmovies.controller.connection.URLBuilderPref;
import com.example.m_alrajab.popularmovies.model_data.MyAdapter;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieDbHelper;
import com.example.m_alrajab.popularmovies.ux.SettingsActivity;
import com.facebook.stetho.Stetho;

/**
 *
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private DisplayMetrics metrics;
    private URLBuilderPref urlBuilderPref;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
        metrics = Resources.getSystem().getDisplayMetrics();
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this.getContext())
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this.getContext()))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this.getContext()))
                            .build()
            );
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        urlBuilderPref=new URLBuilderPref(rootView.getContext());
        rv=(RecyclerView) rootView.findViewById(R.id.rv);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);
        populateMovies();
        if (savedInstanceState==null || !savedInstanceState.containsKey("MoviesInfoSet"))
            updateUIandDB();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putParcelableArrayList("MoviesInfoSet",(ArrayList<MovieItem>)movies);
        super.onSaveInstanceState(outState);
    }

    private int layoutColNum(){
        return Math.round((metrics.widthPixels+5.0f)/urlBuilderPref.getPosterWidth());
    }
    private int getImageHeight(){
        double rt=Math.max((0.01+metrics.heightPixels)/metrics.widthPixels,
                (0.01+metrics.widthPixels)/metrics.heightPixels);
        int hgt=(int)Math.round(getImageWidth()*rt);
        return hgt;
    }
    private int getImageWidth(){
        return Math.max(1,metrics.widthPixels/layoutColNum());
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.popular_movies_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateUIandDB();
            return true;
        }else if (id == R.id.action_settings){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this.getContext(),"Refresh movie list", Toast.LENGTH_LONG).show();
        if (   swipeRefreshLayout.isRefreshing()) {
            updateUI();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       try {
           boolean checkFav = false;
           if (key.equals(this.getActivity().getString(R.string.pref_checked_favorite_key))) {
               for (String ky : sharedPreferences.getAll().keySet())
                   if (ky.startsWith("FAV_") && ((Boolean) sharedPreferences.getAll().get(ky)))
                       checkFav = true;
               SharedPreferences.Editor edt = sharedPreferences.edit();
               if (!checkFav) {
                   edt.putBoolean(getString(R.string.pref_checked_favorite_key), false);
                   Toast.makeText(getContext(), "You have to select at least one movie as favorite",
                           Toast.LENGTH_LONG).show();
               }edt.commit();
           }
           populateMovies();
       }catch (IllegalStateException e){
           e.printStackTrace();
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    private void updateUIandDB()  {
        if(isNetworkAvailable()){
            PopMovieDbHelper f=new PopMovieDbHelper(this.getContext());
            f.onUpgrade(f.getWritableDatabase(),0,0);
            updateUI();
        }else{
            Toast.makeText(getContext(),"Internet connection is required",Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI()  {
            new DataParser(this.getContext(),urlBuilderPref.getAPIURL(),
                    rv.getContext().getResources().getStringArray(R.array.parsingJsonParams)).parseData();
            populateMovies();

    }
    private void populateMovies(){
        try {
            GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this.getContext(), layoutColNum(),
                    GridLayoutManager.VERTICAL, false);
            staggeredGridLayoutManager.setSmoothScrollbarEnabled(true);
            rv.setLayoutManager(staggeredGridLayoutManager);
            MyAdapter adapter=new MyAdapter(this.getContext(), getImageWidth(), getImageHeight());
            rv.setAdapter(adapter);
        }catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("Error in MA Fragment", e.getMessage(), e);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}