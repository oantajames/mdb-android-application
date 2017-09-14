package mdb.com.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author james on 9/4/17.
 */

public class MoviesProvider extends ContentProvider {

    private static final int MOVIES = 101;
    private static final int MOVIE_BY_ID = 102;
    private static final int MOST_POPULAR_MOVIES = 103;
    private static final int TOP_RATED_MOVIES = 104;
    private static final int FAVORITES_MOVIES = 105;
    private static final int TRAILERS = 106;
    private static final int TRAILERS_WITH_MOVIE_ID = 107;
    private static final int REVIEWS = 108;
    private static final int REVIEWS_WITH_MOVIE_ID = 109;

    // movies._id = ?
    private static final String MOVIE_ID_SELECTION =
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID + " = ? ";

    private static final String FAILED_TO_INSERT_ROW_INTO = "Failed to insert row into ";


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MoviesProviderDatabaseHelper dbHelper;

    static {
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIE_BY_ID);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_MOST_POPULAR, MOST_POPULAR_MOVIES);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_TOP_RATED, TOP_RATED_MOVIES);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_FAVORITES, FAVORITES_MOVIES);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_REVIEWS, REVIEWS);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_TRAILERS, TRAILERS);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_TRAILERS + "/#", TRAILERS_WITH_MOVIE_ID);
        //TODO -ADD ALSO FOR REVIEWS WITH ID
    }


    @Override
    public boolean onCreate() {
        dbHelper = new MoviesProviderDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                return MoviesContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_BY_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOST_POPULAR_MOVIES:
                return MoviesContract.MostPopularMoviesEntry.CONTENT_DIR_TYPE;
            case TOP_RATED_MOVIES:
                return MoviesContract.TopRatedMoviesEntry.CONTENT_DIR_TYPE;
            case FAVORITES_MOVIES:
                return MoviesContract.MyFavoritesMoviesEntry.CONTENT_DIR_TYPE;
            default:
                return null;
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        checkColumns(projection);
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                cursor = dbHelper.getReadableDatabase().query(MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_BY_ID:
                cursor = getMovieById(uri, projection, sortOrder);
                break;
            case MOST_POPULAR_MOVIES:
                cursor = getMovieFromReferenceTable(MoviesContract.MostPopularMoviesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, sortOrder);
                break;
            case TOP_RATED_MOVIES:
                cursor = getMovieFromReferenceTable(MoviesContract.TopRatedMoviesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, sortOrder);
                break;
            case FAVORITES_MOVIES:
                cursor = getMovieFromReferenceTable(MoviesContract.MyFavoritesMoviesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, sortOrder);
                break;
            case TRAILERS_WITH_MOVIE_ID:
                long trailerMovieId = ContentUris.parseId(uri);
                selection = String.format("%s = ?", MoviesContract.TrailersEntry.MOVIE_ID);
                selectionArgs = new String[]{String.valueOf(trailerMovieId)};
                cursor = dbHelper.getReadableDatabase().query(MoviesContract.TABLE_TRAILERS,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            case REVIEWS_WITH_MOVIE_ID:
                long reviewsMoviesId = ContentUris.parseId(uri);
                selection = String.format("%s = ?", MoviesContract.TrailersEntry.MOVIE_ID);
                selectionArgs = new String[]{String.valueOf(reviewsMoviesId)};
                cursor = dbHelper.getReadableDatabase().query(MoviesContract.TABLE_REVIEWS,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            default:
                return null;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(
                    MoviesContract.MovieEntry.getColumns()));
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection.");
            }
        }
    }

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {
        long id = MoviesContract.MovieEntry.getIdFromUri(uri);
        String selection = MOVIE_ID_SELECTION;
        String[] selectionArgs = new String[]{Long.toString(id)};
        return dbHelper.getReadableDatabase().query(
                MoviesContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieFromReferenceTable(String tableName, String[] projection,
                                              String selection, String[] selectionArgs, String sortOreder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // tableName INNER JOIN movies ON tableName.movie_id = movies._id
        queryBuilder.setTables(tableName + " INNER JOIN "
                + MoviesContract.MovieEntry.TABLE_NAME
                + " ON " + tableName + "."
                + MoviesContract.COLUMN_MOVIE_ID_KEY + " = "
                + MoviesContract.MovieEntry.TABLE_NAME + "."
                + MoviesContract.MovieEntry._ID);

        return queryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null, null,
                sortOreder);

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        long id;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                id = db.insertWithOnConflict(MoviesContract.MovieEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = MoviesContract.MovieEntry.buildMovieUri(id);
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;
            case MOST_POPULAR_MOVIES:
                id = db.insert(MoviesContract.MostPopularMoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MoviesContract.MostPopularMoviesEntry.CONTENT_URI;
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;
            case TOP_RATED_MOVIES:
                id = db.insert(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MoviesContract.TopRatedMoviesEntry.CONTENT_URI;
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;
            case FAVORITES_MOVIES:
                id = db.insert(MoviesContract.MyFavoritesMoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MoviesContract.MyFavoritesMoviesEntry.CONTENT_URI;
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_BY_ID:
                long movieId = MoviesContract.MovieEntry.getIdFromUri(uri);
                rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME, MOVIE_ID_SELECTION, new String[]{Long.toString(movieId)});
                break;
            case MOST_POPULAR_MOVIES:
                rowsDeleted = db.delete(MoviesContract.MostPopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIES:
                rowsDeleted = db.delete(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES_MOVIES:
                rowsDeleted = db.delete(MoviesContract.MyFavoritesMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + null);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                rowsUpdated = db.update(MoviesContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri :" + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String tableName;
        int insertedRows = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                tableName = MoviesContract.MovieEntry.TABLE_NAME;
                break;
            case REVIEWS:
                tableName = MoviesContract.ReviewsEntry.TABLE_NAME;
                break;
            case TRAILERS:
                tableName = MoviesContract.TrailersEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        db.beginTransaction();
        try {
            for (ContentValues contentValue : values) {
                long id = db.insertWithOnConflict(tableName,
                        null, contentValue, SQLiteDatabase.CONFLICT_REPLACE);
                if (id != -1) {
                    insertedRows++;
                } else {
                    throw new SQLException(String.format("Could not insert %s with id %s ",
                            tableName, contentValue.toString()));
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertedRows;

    }
}
