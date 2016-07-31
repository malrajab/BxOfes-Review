package com.example.m_alrajab.popularmovies.view_UX;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import com.example.m_alrajab.popularmovies.model_data.MovieItem;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    private final String SELECTED_ITEM_KEY="movie_key";
    private  String urlPosterApi;
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor ;
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
        final MovieItem movieItem=intent.getParcelableExtra(SELECTED_ITEM_KEY);
        final String tempURL=intent.getStringExtra("urlPosterApi");
        final String defaultURL=getString(R.string.poster_base_url)+"/w500";
        urlPosterApi=(defaultURL.compareTo(tempURL))>=0?defaultURL:tempURL;
        Picasso.with(view.getContext()).load(urlPosterApi+ movieItem.getPosterImagePath())
                .into((ImageView) view.findViewById(R.id.details_poster));
        ((TextView)view.findViewById(R.id.details_overview)).setText(movieItem.getOverview());
        ((TextView)view.findViewById(R.id.details_title)).setText(movieItem.getTitle());
        ((TextView)view.findViewById(R.id.details_date)).setText(movieItem.getReleaseDate().toString());
        RatingBar ratingBar=(RatingBar)view.findViewById(R.id.movie_ratingbar);
        ratingBar.setRating(movieItem.getUserRating()/2.0f);
        ToggleButton toggleButton=(ToggleButton) view.findViewById(R.id.details_icon_favorite);
        if(sharedPref.getBoolean(String.valueOf(movieItem.getId()),false)){
            toggleButton.setChecked(true);toggleButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
        }
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(((ToggleButton)v).isChecked()) {
                        v.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
                        Toast.makeText(v.getContext(),"Added to your favorite",Toast.LENGTH_SHORT).show();
                        editor.putBoolean(String.valueOf(movieItem.getId()),true);
                    }else {
                        v.setBackgroundResource(R.drawable.ic_favorite_border_black_48dp);
                        Toast.makeText(v.getContext(),"Removed from your favorite",Toast.LENGTH_SHORT).show();
                        editor.putBoolean(String.valueOf(movieItem.getId()),false);
                    }
                    editor.commit();
                    movieItem.setFavorite(((ToggleButton)v).isChecked());// to be activated with DB
                }catch (Exception e) {
                    Log.e("Error details fragment",e.getMessage(),e);
                }
            }
        });
        return view;
    }
}
