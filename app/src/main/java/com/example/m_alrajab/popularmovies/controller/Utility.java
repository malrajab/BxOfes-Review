package com.example.m_alrajab.popularmovies.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.example.m_alrajab.popularmovies.BuildConfig;
import com.example.m_alrajab.popularmovies.R;
import com.facebook.stetho.Stetho;

import java.util.Map;

/**
 * Created by m_alrajab on 8/12/16.
 * provides methods for generic use
 * make sure to pass the context of the activity to ensure correct output
 */

public class Utility {
    private static DisplayMetrics metrics;

    public static void validateChangeOfFavListingIfExist
            (Context context, SharedPreferences sPref, String key){
        try {
            if (key.equals(context.getString(R.string.pref_checked_favorite_key))
                    &&   getNumOfFavMovies(sPref)==0) {
                SharedPreferences.Editor editor = sPref.edit();
                editor.putBoolean(context.getString(R.string.pref_checked_favorite_key), false);
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.no_fav_dialog_title)).setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage(context.getString(R.string.no_fav_dialog_msg))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}}).show();
                editor.commit();

            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getNumOfFavMovies(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return getNumOfFavMovies(sharedPref);
    }

    public static int getNumOfFavMovies(SharedPreferences sharedPref){
        int count=0;
        Map<String,?> items=sharedPref.getAll();
        for(String key:items.keySet())
            if(key.startsWith("FAV_") && ((Boolean) items.get(key)))
                count++;
        return count;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int layoutColNum(Context context) {
        try {
            metrics = Resources.getSystem().getDisplayMetrics();
            return Math.max(1,Math.round((metrics.widthPixels + 5.0f)
                    / (getPrefPosterWidth(context)*getLayoutCol(context))));
        }catch(NullPointerException e){
            e.printStackTrace();
            return 1;
        }
    }

    public static int getImageHeight(Context context) {
        metrics = Resources.getSystem().getDisplayMetrics();
        double rt = Math.max((0.01 + metrics.heightPixels) / metrics.widthPixels,
                (0.01 + metrics.widthPixels) / metrics.heightPixels);
        int hgt = (int) Math.round(getImageWidth(context) * rt);
        return hgt/getLayoutCol(context);
    }

    public static int getImageWidth(Context context) {
        metrics = Resources.getSystem().getDisplayMetrics();
        return Math.max(1, metrics.widthPixels / layoutColNum(context));
    }

    private static int getPrefPosterWidth(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String syncConnPref = sharedPref.getString(context.getString(R.string.pref_poster_res_key), "/w500");
        int width = Integer.parseInt(syncConnPref.split("w")[1]);
        return width;
    }
    private static int getLayoutCol(Context context) {
        return Integer.valueOf(context.getString(R.string.layout_col));
    }

    public static void setStethoWatch(Context context) {
        if(BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(context)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                            .build()
            );
        }
    }
}
