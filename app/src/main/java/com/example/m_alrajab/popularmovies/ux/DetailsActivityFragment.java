package com.example.m_alrajab.popularmovies.ux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemReviewEntry;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.MovieItemTrailerEntry;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    private final String SELECTED_ITEM_KEY="movie_key";
    private  String urlPosterApi, backposter;
    private SharedPreferences sharedPref;
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
    public String[] projectionsReviewsOfMovie={
            MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR,
            MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT
    };
    public String[] projectionsTrailersOfMovie={
            MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_NAME,
            MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_KEY
    };
    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        editor = sharedPref.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_details, container, false);

        Intent intent=getActivity().getIntent();
        final int _id=intent.getIntExtra(SELECTED_ITEM_KEY,0);
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
        Picasso.with(view.getContext()).load(urlPosterApi+ cursor.getString(6))
                .into((ImageView) view.findViewById(R.id.details_poster));
        ((TextView)view.findViewById(R.id.details_overview)).setText(cursor.getString(2));
      //  ((TextView)view.findViewById(R.id.details_title)).setText("Rating: ");
        ((TextView)view.findViewById(R.id.details_date)).setText(cursor.getString(5));
        RatingBar ratingBar=(RatingBar)view.findViewById(R.id.movie_ratingbar);
        ratingBar.setRating(cursor.getFloat(4)/2.0f);
        ToggleButton toggleButton=(ToggleButton) view.findViewById(R.id.details_icon_favorite);
        if(sharedPref.getBoolean(String.valueOf(_id),false)){
            toggleButton.setChecked(true);toggleButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
        }
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(((ToggleButton)v).isChecked()) {
                        v.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
                        Toast.makeText(v.getContext(),"Added to your favorite",Toast.LENGTH_SHORT).show();
                        editor.putBoolean(String.valueOf(_id),true);
                    }else {
                        v.setBackgroundResource(R.drawable.ic_favorite_border_black_48dp);
                        Toast.makeText(v.getContext(),"Removed from your favorite",Toast.LENGTH_SHORT).show();
                        editor.putBoolean(String.valueOf(_id),false);
                    }
                    editor.commit();
                    //movieItem.setFavorite(((ToggleButton)v).isChecked());// to be activated with DB
                }catch (Exception e) {
                    Log.e("Error details fragment",e.getMessage(),e);
                }
            }
        });
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
    }
}
