package com.example.m_alrajab.popularmovies.model_data;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m_alrajab.popularmovies.R;
import com.example.m_alrajab.popularmovies.controller.PrefUrlBuilder;
import com.example.m_alrajab.popularmovies.controller.Utility;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract;
import com.example.m_alrajab.popularmovies.ux.DetailsActivity;
import com.example.m_alrajab.popularmovies.ux.DetailsFragmentLand;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import static com.example.m_alrajab.popularmovies.controller.Utility.getNumOfFavMovies;

/**
 * Created by m_alrajab on 7/28/16.
 * This is the adapter of the recycleview
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder>  {
    private SharedPreferences sharedPref;
    private final String SELECTED_ITEM_KEY="movie_key";
    private Context mContext;
    private int imgWdth, imgHght;
    private PrefUrlBuilder prefUrlBuilder;
    private Cursor mCursor;
    private ArrayList<String> favList=new ArrayList<>();
    private ArrayList<Integer> favIDs=new ArrayList<>();
    private ArrayList<MovieItem> movies=new ArrayList<>();

    String[] projections2={
            PopMovieContract.MovieItemEntry.COLUMN_MOVIE_ID,
            PopMovieContract.MovieItemEntry.COLUMN_MOVIE_POSTERPATH,
    };
    public RecycleViewAdapter(Context context, int w, int h) {
        this.imgWdth=w;
        this.imgHght=h;
        this.mContext=context;
        setupBuilder();
    }
    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) return null;
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) this.notifyDataSetChanged();
        return oldCursor;
    }

    private void setupBuilder() {
        try {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            Map<String,?> items=sharedPref.getAll();
            prefUrlBuilder =new PrefUrlBuilder(mContext);
            mCursor= mContext.getContentResolver().query(
                    PopMovieContract.MovieItemEntry.CONTENT_URI.buildUpon().appendPath(
                            sharedPref.getString(mContext.getResources().getString(R.string.pref_sorting_key),
                                    "top_rated")).build(),projections2, null, null, null);

            if(mCursor != null && mCursor.moveToFirst()){
                do{
                    MovieItem item=new MovieItem();
                    item.setId(mCursor.getInt(0));
                    item.setPosterImagePath(mCursor.getString(1));
                    for(String key:items.keySet())
                        if(key.endsWith(String.valueOf(item.getId()))&&(Boolean)items.get(key)){
                            favList.add(item.getPosterImagePath());
                            favIDs.add(item.getId());
                        }
                    movies.add(item);
                }while (mCursor.moveToNext());
            }
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
    public void onBindViewHolder(MyViewHolder holder,  int position) {
        final int tmpPosition=position;
        try {
            if (sharedPref.getBoolean(mContext.getString(R.string.pref_checked_favorite_key), false)) {
                Picasso.with(mContext).load(prefUrlBuilder.getPosterApiBaseURL() +
                        favList.get(position)).resize(imgWdth-5, imgHght-5)
                        .centerCrop().into(holder.posterView);
                holder.iconView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
                holder.posterView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        intent.putExtra("FAV_SIZE", favIDs.size());
                        intent.putExtra(SELECTED_ITEM_KEY, favIDs.get(tmpPosition));
                        intent.putExtra("urlPosterApi", prefUrlBuilder.getPosterApiBaseURL());
                        if(Utility.getLayoutCol(v.getContext())==3)
                            reattachNewFragment(v,prefUrlBuilder.getPosterApiBaseURL(),movies.get(tmpPosition).getId(),favIDs.size() );
                        else
                            v.getContext().startActivity(intent);
                    }
                });
            } else {
                Picasso.with(mContext).load(prefUrlBuilder.getPosterApiBaseURL() +
                        movies.get(position).getPosterImagePath()).resize(imgWdth, imgHght) // resize the image to these dimensions (in pixel)
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
                        intent.putExtra(SELECTED_ITEM_KEY, movies.get(tmpPosition).getId());
                        intent.putExtra("urlPosterApi", prefUrlBuilder.getPosterApiBaseURL());
                        if(Utility.getLayoutCol(v.getContext())==3)
                            reattachNewFragment(v,prefUrlBuilder.getPosterApiBaseURL(),movies.get(tmpPosition).getId(),3 );
                        else
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
        return mCursor==null?0:sharedPref.getBoolean(
         mContext.getString(R.string.pref_checked_favorite_key),false)
                ?getNumOfFavMovies(mContext):movies.size();
    }


    private void reattachNewFragment(View view,String urlPosterApi, int key, int itmCount){
        DetailsFragmentLand newFragment = new DetailsFragmentLand();
        Bundle args = new Bundle();
        args.putInt(DetailsFragmentLand.ARG_COUNT, itmCount);
        args.putInt(DetailsFragmentLand.ARG_KEY, key);
        args.putString(DetailsFragmentLand.ARG_URL, urlPosterApi);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getActivity(view)
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment3, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // helping method to get the view context parents for the fragment manager
    private AppCompatActivity getActivity(View v) {
        Context c = v.getContext();
        while (c instanceof ContextWrapper) {
            if (c instanceof AppCompatActivity) return (AppCompatActivity)c;
            c = ((ContextWrapper)c).getBaseContext();
        }
        return null;
    }
}
