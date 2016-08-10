package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m_alrajab.popularmovies.R;

/**
 * Created by m_alrajab on 8/9/16.
 */

public class ReviewAdapter extends CursorAdapter {
    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return view;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView)view.findViewById(R.id.review_author)).setText(cursor.getString(1));
        ((TextView)view.findViewById(R.id.review_content)).setText(cursor.getString(2));
    }
}