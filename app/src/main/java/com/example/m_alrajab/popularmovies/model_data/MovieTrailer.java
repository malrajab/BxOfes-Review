package com.example.m_alrajab.popularmovies.model_data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m_alrajab on 8/9/16.
 */

public class MovieTrailer implements Parcelable {
    private int tID;
    private String tKey;
    private String tName;
    private String tSite;

    public int gettID() {
        return tID;
    }

    public void settID(int tID) {
        this.tID = tID;
    }

    public String gettKey() {
        return tKey;
    }

    public void settKey(String tKey) {
        this.tKey = tKey;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettSite() {
        return tSite;
    }

    public void settSite(String tSite) {
        this.tSite = tSite;
    }


    protected MovieTrailer(Parcel in) {
        tID = in.readInt();
        tKey = in.readString();
        tName = in.readString();
        tSite = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tID);
        dest.writeString(tKey);
        dest.writeString(tName);
        dest.writeString(tSite);
    }
}
