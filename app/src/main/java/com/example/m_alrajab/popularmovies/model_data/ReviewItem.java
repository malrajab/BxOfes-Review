package com.example.m_alrajab.popularmovies.model_data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m_alrajab on 8/9/16.
 */

public class ReviewItem implements Parcelable {
    private int rID; // review id
    private String rAuthor; // the author of a review
    private String rContent; // the body of the review

    public String getrContent() {
        return rContent;
    }

    public void setrContent(String rContent) {
        this.rContent = rContent;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public String getrAuthor() {
        return rAuthor;
    }

    public void setrAuthor(String rAuthor) {
        this.rAuthor = rAuthor;
    }


    protected ReviewItem(Parcel in) {
        rID = in.readInt();
        rAuthor = in.readString();
        rContent = in.readString();
    }

    public static final Creator<ReviewItem> CREATOR = new Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rID);
        dest.writeString(rAuthor);
        dest.writeString(rContent);
    }
}
