package com.example.m_alrajab.popularmovies.model_data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.m_alrajab.popularmovies.R;

/**
 * Created by m_alrajab on 7/28/16.
 * simple class for mapping the poster imageview to the member class
 * and the same for the favorite icon on top of the poster
 */
public class MyViewHolder extends RecyclerView.ViewHolder
{
    ImageView posterView;
    ImageView iconView;

    public MyViewHolder(View itemView) {
        super(itemView);
        posterView = (ImageView) itemView.findViewById(R.id.image_view_holder);
        iconView = (ImageView) itemView.findViewById(R.id.favorite_icon);
    }
}