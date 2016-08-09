package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.connection.URLBuilderPref;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract;
import com.example.m_alrajab.popularmovies.ux.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by m_alrajab on 7/28/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>  {
    private SharedPreferences sharedPref;
    private final String SELECTED_ITEM_KEY="movie_key";
    private Context mContext;
    ArrayList<Integer> index=new ArrayList<>();
    URLBuilderPref urlBuilderPref;
    Cursor cursor;

    String[] projections2={
            PopMovieContract.MovieItemEntry.COLUMN_MOVIE_ID,
            PopMovieContract.MovieItemEntry.COLUMN_MOVIE_POSTERPATH,
    };

    public MyAdapter(Context context) {
        this.mContext=context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        urlBuilderPref=new URLBuilderPref(context);
        cursor= context.getContentResolver().query(
                PopMovieContract.MovieItemEntry.CONTENT_URI.buildUpon().appendPath(
                  sharedPref.getString(context.getResources().getString(R.string.pref_sorting_key),
                          "top_rated")).build(),projections2, null, null, null);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recycleview_card_model,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(position==0 ) cursor.moveToFirst();index.add(cursor.getInt(0));
        Picasso.with(mContext).load(urlBuilderPref.getPosterApiBaseURL()+ cursor.getString(1)).into(holder.posterView);

        if(sharedPref.getBoolean(String.valueOf(cursor.getInt(0)),false)){
            holder.iconView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
            holder.iconView.setColorFilter(R.color.colorYellowForFavorite, PorterDuff.Mode.XOR);
        }else{
            holder.iconView.setImageDrawable(null);
            holder.iconView.setColorFilter(R.color.colorYellowForFavorite, PorterDuff.Mode.CLEAR);
        }
        holder.posterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(SELECTED_ITEM_KEY,index.get(position));
                intent.putExtra("urlPosterApi",urlBuilderPref.getPosterApiBaseURL());
                v.getContext().startActivity(intent);
            }
        });
        if(position<getItemCount())cursor.moveToNext();
    }
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
