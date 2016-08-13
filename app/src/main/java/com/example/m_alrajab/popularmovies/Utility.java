package com.example.m_alrajab.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

/**
 * Created by m_alrajab on 8/12/16.
 * provides methods for generic use
 * make sure to pass the context of the activity to ensure correct output
 */

public class Utility {
    private static DisplayMetrics metrics;

    public static void validateChangeOfFavListingIfExist
            (Context context, SharedPreferences sPref, String key){
        boolean checkFav=false;
        try {
        if (key.equals(context.getString(R.string.pref_checked_favorite_key))) {
            for (String favKey : sPref.getAll().keySet())
                if (favKey.startsWith("FAV_") && ((Boolean) sPref.getAll().get(favKey)))
                    checkFav = true;
            if (!checkFav) {
                SharedPreferences.Editor editor = sPref.edit();
                editor.putBoolean(context.getString(R.string.pref_checked_favorite_key), false);
                Toast.makeText(context, "You have to select at least one movie as favorite",
                        Toast.LENGTH_LONG).show();
                editor.commit();
            }
        }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int layoutColNum(Context context) {
        metrics = Resources.getSystem().getDisplayMetrics();
        return Math.max(1,Math.round((metrics.widthPixels + 5.0f) / getPrefPosterWidth(context)));
    }

    public static int getImageHeight(Context context) {
        metrics = Resources.getSystem().getDisplayMetrics();
        double rt = Math.max((0.01 + metrics.heightPixels) / metrics.widthPixels,
                (0.01 + metrics.widthPixels) / metrics.heightPixels);
        int hgt = (int) Math.round(getImageWidth(context) * rt);
        return hgt;
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
