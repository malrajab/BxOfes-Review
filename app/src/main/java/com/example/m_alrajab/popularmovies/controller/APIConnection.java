package com.example.m_alrajab.popularmovies.controller;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by m_alrajab on 7/28/16.
 */
public class APIConnection extends AsyncTask<Void, Void, String> {

    private final String LOG_TAG = this.getClass().toString();
    private BufferedReader reader;
    private String pathUrl;

    public APIConnection(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Nullable
    @Override
    protected String doInBackground(Void... params) {
        return requestData();
    }

    @Override
    protected void onPostExecute(@Nullable String data) {
        super.onPostExecute(data);
    }

    @Nullable
    private String requestData() {

        Log.v(">>>>>", "OOOOOOO >" +pathUrl);
        HttpURLConnection httpURlconnection = null;
        try {

            httpURlconnection = ConnectorAssit.connect(pathUrl);
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
    private static class ConnectorAssit {
        /**
         * connect method establishes the httpURLconnection get method
         * it will timeout if bad connection
         * @param urlPath a string value
         * @return httpURLconnection
         */
        static HttpURLConnection connect(String urlPath) throws IOException {

            URL url=new URL(urlPath);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(HttpURLConnection.HTTP_REQ_TOO_LONG);
            httpURLConnection.setReadTimeout(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return httpURLConnection;

        }
    }

}
