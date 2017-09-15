package mdb.com.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import javax.inject.Inject;

import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.db.MoviesContract;


public class FavoritesRepository {

    protected final Context context;

    @Inject
    public FavoritesRepository(Context context) {
        this.context = context;
    }

    public void addToFavorites(MovieEntity movie) {
        context.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, movie.convertToContentValues());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.COLUMN_MOVIE_ID_KEY, movie.getId());
        context.getContentResolver().insert(MoviesContract.MyFavoritesMoviesEntry.CONTENT_URI, contentValues);
    }

    public void removeFromFavorites(MovieEntity movie) {
        context.getContentResolver().delete(
                MoviesContract.MyFavoritesMoviesEntry.CONTENT_URI,
                MoviesContract.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
                null
        );
    }

    public boolean isFavorite(MovieEntity movie) {
        boolean favorite = false;
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.MyFavoritesMoviesEntry.CONTENT_URI,
                null,
                MoviesContract.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
                null,
                null
        );
        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();
        }
        return favorite;
    }

}
