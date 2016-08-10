package com.example.m_alrajab.popularmovies.model_data.data;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by m_alrajab on 8/3/16.
 */
public class PopMovieContract {
    //content path is for content provider to fetch
    public static final String CONTENT_AUTHORITY = "com.example.m_alrajab.popularmovies";
    //create base of all uris which app will use to contact the provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //possible paths for uri (appended to base uri)
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_MOVIE_REVIEW = "review";
    public static final String PATH_MOVIE_TRAILER = "trailer";

    //inner class to define table contents of movie table
    public static final class MovieItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        //table name
        public static final String TABLE_NAME = "movie";

        //id of the movie according to the api, used to find reviews for said movie
        public static final String COLUMN_MOVIE_ID = "movie_id";
        //public static final String COLUMN_MOVIE_SORT_POP = "sort_popular";
        //public static final String COLUMN_MOVIE_SORT_TOP_RATE = "sort_top_rate";
        //public static final String COLUMN_MOVIE_SORT_FAV = "sort_favorite";
        //public static final String COLUMN_MOVIE_SORT_UPCOMING = "sort_upcoming";
        //public static final String COLUMN_MOVIE_SORT_NOW_PLAYING = "sort_now_playing";
        //title of the movie
        public static final String COLUMN_MOVIE_TITLE = "title"; //"title";
        //overview of the movie
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        //movie rating
        public static final String COLUMN_MOVIE_RATING = "vote_average";
        //movie rating
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";
        //movie release date
        public static final String COLUMN_MOVIE_RELEASE = "release_date";
        //movie poster/backdrop path
        public static final String COLUMN_MOVIE_POSTERPATH = "poster_path";
        public static final String COLUMN_MOVIE_BACKDROPPATH = "backdrop_path";
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

        public static final class MovieItemReviewEntry implements BaseColumns {
            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_REVIEW).build();
            public static final String CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEW;
            //table name
            public static final String TABLE_NAME = "review";
            public static final String COLUMN_MOVIE_REVIEW_ID = "id";
            // Column with the foreign key into the movie table.
            public static final String COLUMN_REVIEW_OF_MOVIE_KEY = "review_movie_id";
            public static final String COLUMN_MOVIE_REVIEW_AUTHOR = "author";
            public static final String COLUMN_MOVIE_REVIEW_CONTENT = "content";
            public static Uri buildMovieUri(long id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }
    }


    public static final class MovieItemTrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_TRAILER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_TRAILER;
        //table name
        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_MOVIE_TRAILER_ID = "id";
        // Column with the foreign key into the movie table.
        public static final String COLUMN_TRAILER_OF_MOVIE_KEY = "trailer_movie_id";
        public static final String COLUMN_MOVIE_TRAILER_KEY = "key";
        public static final String COLUMN_MOVIE_TRAILER_NAME = "name";
        public static final String COLUMN_MOVIE_TRAILER_SITE = "site";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
