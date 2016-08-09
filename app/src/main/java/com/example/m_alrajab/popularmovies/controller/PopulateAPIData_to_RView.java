package com.example.m_alrajab.popularmovies.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.m_alrajab.popularmovies.controller.connection.DataParser;
import com.example.m_alrajab.popularmovies.model_data.MovieItem;
import com.example.m_alrajab.popularmovies.model_data.MyAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by m_alrajab on 7/27/16.
 */
public class PopulateAPIData_to_RView  {
    private Context context;
    private String urlApi;
    private String urlPosterApi;
    private RecyclerView recyclerView;
    private String[] params;
    private ArrayList<MovieItem> movieItemArrayList=new ArrayList<>();

    public PopulateAPIData_to_RView(Context context,String apiUrl ,String urlPosterApi, RecyclerView recyclerView, String[] parsingParams) {
        this.context = context;
        this.params = parsingParams;
        this.recyclerView = recyclerView;
        this.urlApi = apiUrl;
        this.urlPosterApi=urlPosterApi;
    }

    private void onPreExecute() {

        DataParser dataParser=new DataParser(context,urlApi,params);
        try {
            movieItemArrayList=dataParser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        onPreExecute();
        if(movieItemArrayList!=null){
            MyAdapter adapter=new MyAdapter(context, movieItemArrayList, urlPosterApi);
            recyclerView.setAdapter(adapter);
        }
    }
}
