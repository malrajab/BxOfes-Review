package com.example.m_alrajab.popularmovies;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

import com.example.m_alrajab.popularmovies.model_data.data.PopMovieContract;
import com.example.m_alrajab.popularmovies.model_data.data.PopMovieDbHelper;

import java.util.HashSet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public static final String LOG_TAG = ApplicationTest.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(PopMovieDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(PopMovieContract.MovieItemEntry.TABLE_NAME);
        tableNameHashSet.add(PopMovieContract.MovieItemReviewEntry.TABLE_NAME);
        tableNameHashSet.add(PopMovieContract.MovieItemTrailerEntry.TABLE_NAME);

        mContext.deleteDatabase(PopMovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new PopMovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());
        c = db.rawQuery("PRAGMA table_info(" + PopMovieContract.MovieItemReviewEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(PopMovieContract.MovieItemReviewEntry._ID);
        movieColumnHashSet.add(PopMovieContract.MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_AUTHOR);
        movieColumnHashSet.add(PopMovieContract.MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_ID);
        movieColumnHashSet.add(PopMovieContract.MovieItemReviewEntry.COLUMN_REVIEW_OF_MOVIE_KEY);
        movieColumnHashSet.add(PopMovieContract.MovieItemReviewEntry.COLUMN_MOVIE_REVIEW_CONTENT);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }


}
