package com.example.m_alrajab.popularmovies.controller.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.m_alrajab.popularmovies.model_data.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by m_alrajab on 7/28/16.
 */
public class DataParser extends AsyncTask<Void, Void, ArrayList<MovieItem>> {

    private final String LOG_TAG = this.getClass().toString();
    private Context context;
    private String pathURL;
    private String data;
    private String[] parsingParameters;

    private ArrayList<MovieItem> movieItemArrayList=new ArrayList<>();

    public DataParser(Context context, String urlPath, String... parsingParameters) {
        this.context = context;
        this.pathURL = urlPath;
        this.parsingParameters=parsingParameters;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        APIConnection conn=new APIConnection(context,pathURL);
        try {
            data = conn.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(Void... params) {
        return parseData(parsingParameters);
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> isParsed) {
        super.onPostExecute(isParsed);

    }

    private ArrayList<MovieItem> parseData(String... params){
        try{

            JSONObject mJson =new JSONObject(data);
            JSONArray moviesArray = mJson.getJSONArray(params[0]);

            for(int i = 0; i < moviesArray.length(); i++) {
                MovieItem item=new MovieItem();
                JSONObject singleMovie = moviesArray.getJSONObject(i);
                item.setId(singleMovie.getInt(params[1]));
                item.setTitle(singleMovie.getString(params[2]));
                item.setOverview(singleMovie.getString(params[3]));
                item.setUserRating((float)singleMovie.getDouble(params[4]));
                item.setReleaseDate(Date.valueOf(singleMovie.getString(params[5])));
                item.setPosterImagePath(singleMovie.getString(params[6]));
                movieItemArrayList.add(item);
            }

            return movieItemArrayList;
        } catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG,e.getMessage(),e);
        }
        return null;
    }

    public ArrayList<MovieItem> getData(String... strings){
        return movieItemArrayList;
    }
}
