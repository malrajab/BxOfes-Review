package com.example.m_alrajab.popularmovies.ux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.model_data.ReviewAdapter;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemReviewEntry;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String ARG_TYPE = "FRAGMENT_TYPE";
    private ReviewAdapter mReviewAdapter;
    private static final int LOADER_ID = 42;
    private final String SELECTED_ITEM_KEY="movie_key";
    private  String urlPosterApi, mArgType;
    private SharedPreferences sharedPref;
    private int _id;
    private int layout_id=-1;
    SharedPreferences.Editor editor ;
    Cursor cursor;
    private String[] projectionsMovieDetails ={
            MovieItemEntry.COLUMN_MOVIE_ID,
            MovieItemEntry.COLUMN_MOVIE_TITLE,
            MovieItemEntry.COLUMN_MOVIE_OVERVIEW,
            MovieItemEntry.COLUMN_MOVIE_POPULARITY,
            MovieItemEntry.COLUMN_MOVIE_RATING,
            MovieItemEntry.COLUMN_MOVIE_RELEASE,
            MovieItemEntry.COLUMN_MOVIE_POSTERPATH,
            MovieItemEntry.COLUMN_MOVIE_BACKDROPPATH
    };

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        editor = sharedPref.edit();


        Bundle arguments = getArguments();
        if (arguments.containsKey(ARG_TYPE))
            layout_id=(arguments.getString(ARG_TYPE).equals("Details"))
                    ?R.layout.fragment_details:R.layout.fragment_details2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(layout_id, container, false);
        Intent intent=getActivity().getIntent();
        _id=intent.getIntExtra(SELECTED_ITEM_KEY,0);
        final String tempURL=intent.getStringExtra("urlPosterApi");
        cursor= this.getContext().getContentResolver().query(
                MovieItemEntry.CONTENT_URI.buildUpon().appendPath(
                        sharedPref.getString(this.getContext().getString(R.string.pref_sorting_key),
                                "top_rated")).build()
                , projectionsMovieDetails,
                MovieItemEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{String.valueOf(_id)}, null);
        if(cursor==null)
            return null;
        cursor.moveToFirst();
        final String defaultURL=getString(R.string.poster_base_url)+"/w500";
        urlPosterApi=(defaultURL.compareTo(tempURL))>=0?defaultURL:tempURL;
        if(layout_id==R.layout.fragment_details) {
            Picasso.with(view.getContext()).load(urlPosterApi + cursor.getString(6))
                    .into((ImageView) view.findViewById(R.id.details_poster));
            ((TextView) view.findViewById(R.id.details_overview)).setText(cursor.getString(2));
            ((TextView) view.findViewById(R.id.details_date)).setText(cursor.getString(5));
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.movie_ratingbar);
            ratingBar.setRating(cursor.getFloat(4) / 2.0f);
            ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.details_icon_favorite);
            if (sharedPref.getBoolean(String.valueOf(_id), false)) {
                toggleButton.setChecked(true);
                toggleButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
            }
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (((ToggleButton) v).isChecked()) {
                            v.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
                            Toast.makeText(v.getContext(), "Added to your favorite", Toast.LENGTH_SHORT).show();
                            editor.putBoolean(String.valueOf(_id), true);
                        } else {
                            v.setBackgroundResource(R.drawable.ic_favorite_border_black_48dp);
                            Toast.makeText(v.getContext(), "Removed from your favorite", Toast.LENGTH_SHORT).show();
                            editor.putBoolean(String.valueOf(_id), false);
                        }
                        editor.commit();
                        //movieItem.setFavorite(((ToggleButton)v).isChecked());// to be activated with DB
                    } catch (Exception e) {
                        Log.e("Error details fragment", e.getMessage(), e);
                    }
                }
            });
        }else{
            mReviewAdapter=new ReviewAdapter(getActivity(),R.layout.review_item,null,
                    new String[]{MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR,MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT },
                    new int[]{R.id.review_author,R.id.review_content},0);
            ListView listView = (ListView) view.findViewById(R.id.details_review_list);
            listView.setAdapter(mReviewAdapter);
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(cursor.moveToFirst()) {
            ((CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout)).setTitle(cursor.getString(1));
            Picasso.with(getContext()).load(urlPosterApi+ cursor.getString(7))
                    .into((ImageView) getActivity().findViewById(R.id.backdrop_container));
        }
        if(layout_id==R.layout.fragment_details2)
            getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i != LOADER_ID) {
            return null;
        }
        return new CursorLoader(getActivity(),
                MovieItemReviewEntry.CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(_id)+"/reviews")
                        .build(), null, MovieItemReviewEntry.COLUMN_REVIEW_OF_MOVIE_KEY+ " = ? ",
                new String[]{String.valueOf(_id)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mReviewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mReviewAdapter.swapCursor(null);
    }
}
