package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.connection.URLBuilderPref;
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
    ArrayList<MovieItem> movies;
    URLBuilderPref urlBuilderPref;


    public MyAdapter(Context context, ArrayList<MovieItem> movies) {
        this.mContext=context;
        this.movies=movies;
        urlBuilderPref=new URLBuilderPref(mContext);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.recycleview_card_model,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.with(mContext).load(urlBuilderPref.getPosterApiBaseURL()+ movies.get(position).getPosterImagePath()).into(holder.posterView);
        //if(movies.get(position).isFavorite()) {
        // to be implemented with the DB
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(sharedPref.getBoolean(String.valueOf(movies.get(position).getId()),false)){
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
                intent.putExtra(SELECTED_ITEM_KEY,movies.get(position));
                intent.putExtra("urlPosterApi",urlBuilderPref.getPosterApiBaseURL());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}
