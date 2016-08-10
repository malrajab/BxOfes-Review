package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m_alrajab.popularmovies.R;

/**
 * Created by m_alrajab on 8/9/16.
 */

public class ReviewAdapter extends SimpleCursorAdapter {


    public ReviewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

    }

    private static class ViewHolder {
        final TextView review_author;
        final TextView review_content;

        ViewHolder(View view) {
            review_author = (TextView) view.findViewById(R.id.review_author);
            review_content = (TextView) view.findViewById(R.id.review_content);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        //check for odd or even to set alternate colors to the row background
        if(position % 2 == 0){
            view.setBackgroundColor(Color.rgb(255, 210, 213));
        }
        else {
            view.setBackgroundColor(Color.rgb(210, 210, 255));
        }
        return view;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag( viewHolder);
        Log.v("adapt ", "new Bind");
        return view;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.review_author.setText(cursor.getString(3));
        viewHolder.review_content.setText(cursor.getString(4));
        Log.v("adapt ", " Bind"+getCount());
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}