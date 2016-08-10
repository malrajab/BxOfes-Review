package com.example.m_alrajab.popularmovies.ux;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.m_alrajab.popularmovies.R;

public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //getActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        if(getSupportActionBar() != null){

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);}
        setSupportActionBar(toolbar);

    }

}
