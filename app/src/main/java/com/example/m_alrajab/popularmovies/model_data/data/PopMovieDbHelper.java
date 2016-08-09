package com.example.m_alrajab.popularmovies.model_data.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract.*;

/**
 * Created by m_alrajab on 8/3/16.
 */
    public class PopMovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "popularmovies.db";


    //need to add poster and bd to db, this db is for favs only.
    public PopMovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        final String SQL_CREATE_MOVIE_REVIEW_TABLE =
           "CREATE TABLE " + MovieItemReviewEntry.TABLE_NAME + " (" +
                MovieItemReviewEntry._ID                            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieItemReviewEntry.COLUMN_REVIEW_OF_MOVIE_KEY     + " INTEGER NOT NULL, " +
                MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_ID         + " TEXT UNIQUE NOT NULL, " +
                MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR     + " TEXT NOT NULL, " +
                MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT    + " TEXT NOT NULL, " +
                   // Set up the review column as a foreign key to review table.
                   " FOREIGN KEY (" + MovieItemReviewEntry.COLUMN_REVIEW_OF_MOVIE_KEY + ") REFERENCES " +
                   MovieItemReviewEntry.TABLE_NAME + " (" + MovieItemEntry._ID + ")  );";

        final String SQL_CREATE_MOVIE_TRAILER_TABLE =
           "CREATE TABLE " + MovieItemTrailerEntry.TABLE_NAME + " (" +
                MovieItemTrailerEntry._ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieItemTrailerEntry.COLUMN_TRAILER_OF_MOVIE_KEY   + " INTEGER NOT NULL, " +
                MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_ID       + " TEXT UNIQUE NOT NULL, " +
                MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_KEY      + " TEXT NOT NULL, " +
                MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_NAME     + " TEXT NOT NULL, " +
                MovieItemTrailerEntry.COLUMN_MOVIE_TRAILER_SITE     + " TEXT NOT NULL, " +
                   // Set up the trailer column as a foreign key to trailer table.
                   " FOREIGN KEY (" + MovieItemTrailerEntry.COLUMN_TRAILER_OF_MOVIE_KEY + ") REFERENCES " +
                   MovieItemTrailerEntry.TABLE_NAME + " (" + MovieItemEntry._ID + ") );";

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieItemEntry.TABLE_NAME
                + " (" +
                MovieItemEntry._ID                      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieItemEntry.COLUMN_MOVIE_ID                      + "  INTEGER NOT NULL, " +
                //MovieItemEntry.COLUMN_MOVIE_SORT_POP                + "  TEXT, " +
                //MovieItemEntry.COLUMN_MOVIE_SORT_FAV                + "  TEXT, " +
                //MovieItemEntry.COLUMN_MOVIE_SORT_NOW_PLAYING        + "  TEXT, " +
                //MovieItemEntry.COLUMN_MOVIE_SORT_TOP_RATE           + "  TEXT, " +
                //MovieItemEntry.COLUMN_MOVIE_SORT_UPCOMING           + "  TEXT, " +
                MovieItemEntry.COLUMN_MOVIE_TITLE                   + " TEXT NOT NULL, " +
                MovieItemEntry.COLUMN_MOVIE_OVERVIEW                + " TEXT NOT NULL, " +
                MovieItemEntry.COLUMN_MOVIE_POPULARITY               + " REAL NOT NULL, " +
                MovieItemEntry.COLUMN_MOVIE_POSTERPATH              + " TEXT NOT NULL, " +
                MovieItemEntry.COLUMN_MOVIE_BACKDROPPATH            + " TEXT NOT NULL, " +
                MovieItemEntry.COLUMN_MOVIE_RATING                  + " REAL NOT NULL, " +
                MovieItemEntry.COLUMN_MOVIE_RELEASE                 + " TEXT NOT NULL, UNIQUE ( " +
                MovieItemEntry.COLUMN_MOVIE_ID                      + ") );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieItemReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieItemTrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
