package mdb.com.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author james on 9/4/17.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDatabaseProvider";

    private static final int DATABASE_SCHEMA_VERSION = 4;
    private static final String SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoviesContract.MovieEntry.SQL_CREATE_TABLE_MOVIES);
        db.execSQL(MoviesContract.MostPopularMoviesEntry.SQL_CREATE_TABLE);
        db.execSQL(MoviesContract.TopRatedMoviesEntry.SQL_CREATE_TABLE);
        db.execSQL(MoviesContract.MyFavoritesMoviesEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MovieEntry.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MostPopularMoviesEntry.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.TopRatedMoviesEntry.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MyFavoritesMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
