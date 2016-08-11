package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.connection.URLBuilderPref;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract;
import com.example.m_alrajab.popularmovies.ux.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by m_alrajab on 7/28/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>  {
    private SharedPreferences sharedPref;
    private final String SELECTED_ITEM_KEY="movie_key";
    private Context mContext;
    private int numFavMovies=0, ii=0, imgWdth, imgHght;
    URLBuilderPref urlBuilderPref;
    Cursor cursor;
    ArrayList<String> favList=new ArrayList<>();
    ArrayList<Integer> favIDs=new ArrayList<>();
    ArrayList<MovieItem> movies=new ArrayList<>();

    String[] projections2={
            PopMovieContract.MovieItemEntry.COLUMN_MOVIE_ID,
            PopMovieContract.MovieItemEntry.COLUMN_MOVIE_POSTERPATH,
    };

    public MyAdapter(Context context) {
        this.mContext=context;
        setupBuilder();
    }

    public MyAdapter(Context context, int w, int h) {
        this.imgWdth=w;
        this.imgHght=h;
        this.mContext=context;
        setupBuilder();
    }

    private void setupBuilder() {

        try {


            sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            Map<String,?> items=sharedPref.getAll();
            urlBuilderPref=new URLBuilderPref(mContext);
            cursor= mContext.getContentResolver().query(
                    PopMovieContract.MovieItemEntry.CONTENT_URI.buildUpon().appendPath(
                            sharedPref.getString(mContext.getResources().getString(R.string.pref_sorting_key),
                                    "top_rated")).build(),projections2, null, null, null);

            if(cursor.moveToFirst()){

                do{
                    MovieItem item=new MovieItem();
                    item.setId(cursor.getInt(0));
                    item.setPosterImagePath(cursor.getString(1));
                    for(String key:items.keySet()){
                        if(key.endsWith(String.valueOf(item.getId()))) {
                            if((Boolean)items.get(key)) {
                                favList.add(item.getPosterImagePath());
                                favIDs.add(item.getId());
                            }
                        }
                    }
                    movies.add(item);
                }while (cursor.moveToNext());
            }
            for(String key:items.keySet()){
                if(key.startsWith("FAV_")) {
                    if((Boolean)items.get(key)) {
                        ii++;
                    }
                }
            }
            numFavMovies=ii;
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recycleview_card_model,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.v("fffff", " "+imgWdth+" ,, "+ imgHght);
        try {
            if (sharedPref.getBoolean(mContext.getString(R.string.pref_checked_favorite_key), false)) {
                Picasso.with(mContext).load(urlBuilderPref.getPosterApiBaseURL() +
                        favList.get(position)).resize(imgWdth-5, imgHght-5) // resizes the image to these dimensions (in pixel)
                        .centerCrop().into(holder.posterView);
                holder.iconView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_white_24dp));

                holder.posterView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        intent.putExtra("FAV_SIZE", favIDs.size());
                        intent.putExtra(SELECTED_ITEM_KEY, favIDs.get(position));
                        intent.putExtra("urlPosterApi", urlBuilderPref.getPosterApiBaseURL());
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                Picasso.with(mContext).load(urlBuilderPref.getPosterApiBaseURL() +
                        movies.get(position).getPosterImagePath()).resize(imgWdth, imgHght) // resizes the image to these dimensions (in pixel)
                        .centerCrop().into(holder.posterView);
                if (sharedPref.getBoolean(String.valueOf("FAV_" + movies.get(position).getId()), false)) {
                    holder.iconView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
                } else {
                    holder.iconView.setImageDrawable(null);
                }
                holder.posterView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        intent.putExtra(SELECTED_ITEM_KEY, movies.get(position).getId());
                        intent.putExtra("urlPosterApi", urlBuilderPref.getPosterApiBaseURL());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    public int getItemCount() {
        if(sharedPref.getBoolean(mContext.getString(R.string.pref_checked_favorite_key),false))
            return  numFavMovies;
        else
            return movies.size();
    }


}
