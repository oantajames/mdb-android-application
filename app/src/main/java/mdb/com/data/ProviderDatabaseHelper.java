package mdb.com.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author james on 9/1/17.
 */

public class ProviderDatabaseHelper extends SQLiteOpenHelper {

    private static ProviderDatabaseHelper instance;

    private static final String DATABASE_NAME = "movieDataBase";
    private static final int DATABASE_VERSION = 1;

    public ProviderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TABLE_MOVIES = String.format("CREATE TABLE %s"
                    + "(%S INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s BOOLEAN, %s BOOLEAN, %s TEXT, %s TEXT,"
                    + "%s TEXT, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT,   %s DOUBLE, %s INTEGER, %s BOOLEAN)",
            DatabaseContract.TABLE_MOVIES,
            DatabaseContract.MovieColumns._ID,
            DatabaseContract.MovieColumns.BACKDROP_PATH,
            DatabaseContract.MovieColumns.HAS_VIDEO,
            DatabaseContract.MovieColumns.IS_ADULT,
            DatabaseContract.MovieColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.MovieColumns.ORIGINAL_TITLE,
            DatabaseContract.MovieColumns.OVERVIEW,
            DatabaseContract.MovieColumns.POPULARITY,
            DatabaseContract.MovieColumns.POSTER_PATH,
            DatabaseContract.MovieColumns.RELEASE_DATE,
            DatabaseContract.MovieColumns.TITLE,
            DatabaseContract.MovieColumns.VOTE_AVERAGE,
            DatabaseContract.MovieColumns.VOTE_COUNT,
            DatabaseContract.MovieColumns.IS_FAVORITE);

    private static final String SQL_CREATE_TABLE_REVIEWS = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)",
            DatabaseContract.TABLE_REVIEWS,
            DatabaseContract.ReviewColumns._ID,
            DatabaseContract.ReviewColumns.AUTHOR,
            DatabaseContract.ReviewColumns.CONTENT,
            DatabaseContract.ReviewColumns.MOVIE_ID,
            DatabaseContract.ReviewColumns.URL
    );

    private static final String SQL_CREATE_TABLE_TRAILERS = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT,  %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT)",
            DatabaseContract.TABLE_TRAILERS,
            DatabaseContract.TrailerColumns._ID,
            DatabaseContract.TrailerColumns.ISO6391,
            DatabaseContract.TrailerColumns.ISO31661,
            DatabaseContract.TrailerColumns.KEY,
            DatabaseContract.TrailerColumns.NAME,
            DatabaseContract.TrailerColumns.MOVIE_ID,
            DatabaseContract.TrailerColumns.SITE,
            DatabaseContract.TrailerColumns.SIZE,
            DatabaseContract.TrailerColumns.TYPE
    );

    public static synchronized ProviderDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ProviderDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL(SQL_CREATE_TABLE_MOVIES);
        db.execSQL(SQL_CREATE_TABLE_REVIEWS);
        db.execSQL(SQL_CREATE_TABLE_TRAILERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_MOVIES);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_REVIEWS);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TRAILERS);
            onCreate(db);
        }
    }

}
