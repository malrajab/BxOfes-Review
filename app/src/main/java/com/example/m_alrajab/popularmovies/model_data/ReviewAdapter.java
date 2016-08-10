package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
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
        Log.v("adapt ", "In");
    }
    public static class ViewHolder {
        public final TextView review_author;
        public final TextView review_content;

        public ViewHolder(View view) {
            review_author = (TextView) view.findViewById(R.id.review_author);
            review_content = (TextView) view.findViewById(R.id.review_content);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        view.setTag( new ViewHolder(view));
        Log.v("adapt ", "new Bind");
        return view;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.review_author.setText(cursor.getString(3));
        viewHolder.review_content.setText(cursor.getString(4));
        Log.v("adapt ", " Bind");
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}