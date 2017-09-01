package mdb.com.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author james on 9/1/17.
 */

public class MovieProvider extends ContentProvider {


    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;
    private static final int MOST_POPULAR_MOVIES = 102;
    private static final int TOP_RATED_MOVIES = 103;
    private static final int FAVORITES_MOVIES = 104;
    private static final int TRAILERS = 105;
    private static final int TRAILERS_WITH_MOVIE_ID = 106;
    private static final int REVIEWS = 107;
    private static final int REVIEWS_WITH_MOVIE_ID = 108;


    private ProviderDatabaseHelper dbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://mdb.com.data/movies
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_MOVIES, MOVIES);

        // content://mdb.com.data/trailers
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_TRAILERS,
                TRAILERS);

        // content://mdb.com.data/reviews
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_REVIEWS,
                REVIEWS);

        // content://mdb.com.data/movies/id
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_MOVIES + "/#",
                MOVIE_WITH_ID);

        // content://mdb.com.data/movies/top_rated
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_MOVIES + "/" +
                DatabaseContract.PATH_TOP_RATED, TOP_RATED_MOVIES);

        // content://mdb.com.data/movies/most_popular
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_MOVIES + "/" +
                DatabaseContract.PATH_MOST_POPULAR, MOST_POPULAR_MOVIES);

        // content://mdb.com.data/movies/favorites
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_MOVIES + "/" +
                DatabaseContract.PATH_FAVORITES, FAVORITES_MOVIES);

        // content://mdb.com.data/reviews/movie_id
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_REVIEWS + "/#",
                REVIEWS_WITH_MOVIE_ID);

        // content://mdb.com.data/trailers/movie_id
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_TRAILERS + "/#",
                TRAILERS_WITH_MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ProviderDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final Cursor result;
        final String table;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                //Rows aren't counted with null selection
                selection = (selection == null) ? "1" : selection;
                table = DatabaseContract.TABLE_MOVIES;
                break;
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);
                selectionArgs = new String[]{String.valueOf(id)};
                table = DatabaseContract.TABLE_MOVIES;
                break;
            case TRAILERS_WITH_MOVIE_ID:
                long trailersMovieId = ContentUris.parseId(uri);
                selection = String.format("%s = ?", DatabaseContract.TrailerColumns.MOVIE_ID);
                selectionArgs = new String[]{String.valueOf(trailersMovieId)};
                table = DatabaseContract.TABLE_TRAILERS;
                break;
            case REVIEWS_WITH_MOVIE_ID:
                long reviewsMovieId = ContentUris.parseId(uri);
                selection = String.format("%s = ?", DatabaseContract.TrailerColumns.MOVIE_ID);
                selectionArgs = new String[]{String.valueOf(reviewsMovieId)};
                table = DatabaseContract.TABLE_REVIEWS;
                break;
            default:
                throw new IllegalArgumentException("Illegal query uri : " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        result = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                long id = db.insertWithOnConflict(DatabaseContract.TABLE_MOVIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI_MOVIES, id);
                } else {
                    throw new SQLException("Filed to insert row into : " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                selection = (selection == null) ? "1" : selection;
                break;
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", DatabaseContract.MovieColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(DatabaseContract.TABLE_MOVIES, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String tableName;
        final int match = uriMatcher.match(uri);
        int insertedRows = 0;
        switch (match) {
            case MOVIES:
                tableName = DatabaseContract.TABLE_MOVIES;
                break;
            case TRAILERS:
                tableName = DatabaseContract.TABLE_TRAILERS;
                break;
            case REVIEWS:
                tableName = DatabaseContract.TABLE_REVIEWS;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                long id = db.insertWithOnConflict(tableName, null, value, SQLiteDatabase.CONFLICT_IGNORE);
                if (id == -1) {
                    throw new SQLException(String.format("Could not insert %s with id %s ", tableName,
                            value.toString()));
                } else {
                    insertedRows++;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLException e) {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return insertedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ? ", DatabaseContract.MovieColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal update URI");
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(DatabaseContract.TABLE_MOVIES, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
