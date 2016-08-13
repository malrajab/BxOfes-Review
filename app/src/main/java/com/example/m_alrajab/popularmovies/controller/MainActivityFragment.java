package com.example.m_alrajab.popularmovies.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.services.PopMoviesService;
import com.example.m_alrajab.popularmovies.controller.services.URLBuilderPref;
import com.example.m_alrajab.popularmovies.model_data.MyAdapter;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieDbHelper;
import com.example.m_alrajab.popularmovies.ux.SettingsActivity;

import static com.example.m_alrajab.popularmovies.Utility.getImageHeight;
import static com.example.m_alrajab.popularmovies.Utility.getImageWidth;
import static com.example.m_alrajab.popularmovies.Utility.isNetworkAvailable;
import static com.example.m_alrajab.popularmovies.Utility.layoutColNum;
import static com.example.m_alrajab.popularmovies.Utility.setStethoWatch;
import static com.example.m_alrajab.popularmovies.Utility.validateChangeOfFavListingIfExist;

/**
 *
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private URLBuilderPref urlBuilderPref;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    SharedPreferences prefs;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        setStethoWatch(getActivity());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.popular_movies_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
           validateChangeOfFavListingIfExist(getActivity(),sharedPreferences,key);
            if(key!=null)
                 populateMovies();
    }

    private void updateUIandDB()  {
        if(isNetworkAvailable(getActivity())){
            PopMovieDbHelper f=new PopMovieDbHelper(this.getContext());
            f.onUpgrade(f.getWritableDatabase(),0,0);
            updateUI();
        }else{
            Toast.makeText(getContext(),"Internet connection is required",Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI()  {
//            new DataParser(this.getContext(),urlBuilderPref.getAPIURL(),
//                    rv.getContext().getResources().getStringArray(R.array.parsingJsonParams)).parseData();
            updateMovieList();
            populateMovies();

    }
    private void populateMovies(){
        try {
            GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this.getContext(),
                    layoutColNum(this.getContext()),GridLayoutManager.VERTICAL, false);
            staggeredGridLayoutManager.setSmoothScrollbarEnabled(true);
            rv.setLayoutManager(staggeredGridLayoutManager);
            MyAdapter adapter=new MyAdapter(this.getContext(), getImageWidth(getActivity()), getImageHeight(getActivity()));
            rv.setAdapter(adapter);
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void updateMovieList() {
        Intent alarmIntent = new Intent(getActivity(), PopMoviesService.AlarmReceiver.class);
        alarmIntent.putExtra(PopMoviesService.MOVIE_SORT_TYPE_EXTRA,
                prefs.getString(getActivity().getString(R.string.pref_sorting_key),
                        getActivity().getString(R.string.pref_sorting_values_default)) );
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0,alarmIntent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 50, pi);
    }



}