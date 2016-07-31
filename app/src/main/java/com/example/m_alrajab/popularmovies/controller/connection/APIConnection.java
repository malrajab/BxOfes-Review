package com.example.m_alrajab.popularmovies.controller.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by m_alrajab on 7/28/16.
 */
public class APIConnection extends AsyncTask<Void, Void, String> {

    private final String LOG_TAG = this.getClass().toString();
    private BufferedReader reader;
    private Context context;
    private String pathUrl;



    public APIConnection(Context context, String pathUrl) {
        this.context = context;
        this.pathUrl = pathUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        return requestData();
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        if(data.startsWith("Error")){
            Toast.makeText(context,"Connection error - no data ", Toast.LENGTH_SHORT).show();
        }
    }

    private String requestData() {
        HttpURLConnection httpURlconnection = (HttpURLConnection) Connector.connect(pathUrl);
        if (httpURlconnection.toString().startsWith("Error")) {
            return httpURlconnection.toString();
        } else {
            try {

                if (httpURlconnection.getResponseCode() ==
                        HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURlconnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    return buffer.toString();


                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (httpURlconnection != null) {
                    httpURlconnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
