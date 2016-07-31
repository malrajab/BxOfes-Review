package com.example.m_alrajab.popularmovies.model_data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by m_alrajab on 7/27/16.
 *
 * original title
 * movie poster image thumbnail
 * A plot synopsis (called overview in the api)
 * user rating (called vote_average in the api)
 * release date
 */


public class MovieItem implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private float userRating;
    private Date releaseDate;
    private String posterImagePath;
    private boolean isFavorite;
    private String trailerKey;

    public MovieItem()
    {
        this("","","");
    }
    public MovieItem(Parcel parcel)
    {
        id=parcel.readInt();
        title=parcel.readString();
        overview=parcel.readString();
        userRating=parcel.readFloat();
        releaseDate=(Date)parcel.readValue(ClassLoader.getSystemClassLoader());
        posterImagePath=parcel.readString();
        isFavorite=Boolean.parseBoolean(parcel.readString());
        trailerKey=parcel.readString();
    }

    public MovieItem(String posterImagePath)
    {
        this("","",posterImagePath);
    }

    public MovieItem(String originalTitle, String movieDescription, String posterImagePath)
    {
        this(0,originalTitle,movieDescription,0,null,posterImagePath);
    }

    public MovieItem(int api_ID, String originalTitle, String movieDescription, float voteAverage,
                     Date releaseDate,  String posterImagePath)
    {
        this.id=api_ID;
        this.title = originalTitle;
        this.overview = movieDescription;
        this.userRating=voteAverage;
        this.releaseDate=releaseDate;
        this.posterImagePath = posterImagePath;
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterImagePath() {
        return posterImagePath;
    }

    public void setPosterImagePath(String posterImagePath) {
        this.posterImagePath = posterImagePath;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeFloat(userRating);
        dest.writeValue(releaseDate);
        dest.writeString(posterImagePath);
        dest.writeString(String.valueOf(isFavorite));
        dest.writeString(trailerKey);

    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public String toString() {
        return "Movie item [id="+id+", title="+title+", ReleaseDate="+releaseDate+
                "] for more info use setters and getters";
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }
}


