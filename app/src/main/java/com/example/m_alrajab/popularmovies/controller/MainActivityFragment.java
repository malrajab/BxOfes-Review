package com.example.m_alrajab.popularmovies.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.m_alrajab.popularmovies.controller.sync.MoviesSyncAdapter;
import com.example.m_alrajab.popularmovies.model_data.RecycleViewAdapter;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract;
import com.example.m_alrajab.popularmovies.ux.SettingsActivity;

import static com.example.m_alrajab.popularmovies.controller.Utility.getImageHeight;
import static com.example.m_alrajab.popularmovies.controller.Utility.getImageWidth;
import static com.example.m_alrajab.popularmovies.controller.Utility.isNetworkAvailable;
import static com.example.m_alrajab.popularmovies.controller.Utility.layoutColNum;
import static com.example.m_alrajab.popularmovies.controller.Utility.setStethoWatch;
import static com.example.m_alrajab.popularmovies.controller.Utility.validateChangeOfFavListingIfExist;

/**
 *
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int RV_LOADER = 10;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private SharedPreferences prefs;
    private RecycleViewAdapter adapter;
    private PrefUrlBuilder prefUrlBuilder;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
        prefUrlBuilder =new PrefUrlBuilder(getActivity());
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
        rv=(RecyclerView) rootView.findViewById(R.id.rv);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (savedInstanceState==null || !savedInstanceState.containsKey("MoviesInfoSet"))
            populateMovies();
        if(!isNetworkAvailable(getActivity()))
            Toast.makeText(getContext(),"Internet connection is required",Toast.LENGTH_LONG).show();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putParcelableArrayList("MoviesInfoSet",(ArrayList<MovieItem>)movies);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(RV_LOADER, null, this);
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
            updateMovieList();
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
            populateMovies();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        validateChangeOfFavListingIfExist(getActivity(),sharedPreferences,key);
        if(key!=null)
            populateMovies();
    }

    private void populateMovies(){
        try {
            GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this.getContext(),
                    layoutColNum(this.getContext()),GridLayoutManager.VERTICAL, false);
            staggeredGridLayoutManager.setSmoothScrollbarEnabled(true);
            rv.setLayoutManager(staggeredGridLayoutManager);
            adapter=new RecycleViewAdapter(this.getContext(),
                    getImageWidth(getActivity()), getImageHeight(getActivity()));
            rv.setAdapter(adapter);
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void updateMovieList() {
        MoviesSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return (id != RV_LOADER)?null: new CursorLoader(getContext(), PopMovieContract.MovieItemEntry
                .CONTENT_URI.buildUpon().appendPath(prefs.getString(getActivity().getResources().getString(R.string.pref_sorting_key),
                        "top_rated")).build(),null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        populateMovies();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


}