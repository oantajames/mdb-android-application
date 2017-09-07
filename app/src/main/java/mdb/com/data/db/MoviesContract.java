package mdb.com.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * @author james on 9/1/17.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "mdb.com.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_MOST_POPULAR = "most_popular";
    public static final String PATH_FAVORITES = "favorites";
    public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

    //Tables
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_REVIEWS = "reviews";
    public static final String TABLE_TRAILERS = "trailers";

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String VOTE_COUNT = "vote_count";
        public static final String HAS_VIDEO = "has_video";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "poster_path";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String IS_ADULT = "is_adult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";

        public static final String[] COLUMNS = {_ID, BACKDROP_PATH, HAS_VIDEO, IS_ADULT,
                ORIGINAL_LANGUAGE, ORIGINAL_TITLE, OVERVIEW, POPULARITY, POSTER_PATH,
                RELEASE_DATE, TITLE, VOTE_AVERAGE, VOTE_COUNT};

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

        public static final String SQL_CREATE_TABLE_MOVIES = String.format("CREATE TABLE %s"
                        + " (%s INTEGER PRIMARY KEY, %s TEXT, %s BOOLEAN, %s BOOLEAN, %s TEXT, %s TEXT,"
                        + "%s TEXT, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT, %s DOUBLE, %s INTEGER )",
                MoviesContract.TABLE_MOVIES,
                MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.BACKDROP_PATH,
                MoviesContract.MovieEntry.HAS_VIDEO,
                MoviesContract.MovieEntry.IS_ADULT,
                MoviesContract.MovieEntry.ORIGINAL_LANGUAGE,
                MoviesContract.MovieEntry.ORIGINAL_TITLE,
                MoviesContract.MovieEntry.OVERVIEW,
                MoviesContract.MovieEntry.POPULARITY,
                MoviesContract.MovieEntry.POSTER_PATH,
                MoviesContract.MovieEntry.RELEASE_DATE,
                MoviesContract.MovieEntry.TITLE,
                MoviesContract.MovieEntry.VOTE_AVERAGE,
                MoviesContract.MovieEntry.VOTE_COUNT);
    }

    // TOP RATED MOVIES TABLE
    public static final class TopRatedMoviesEntry implements BaseColumns {

        private static final String[] COLUMNS = {_ID, COLUMN_MOVIE_ID_KEY};

        private TopRatedMoviesEntry() {
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

        public static final String TABLE_NAME = "top_rated_movies";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED)
                .build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
                + "/" + PATH_TOP_RATED;

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " + " );";
    }

    // MOST POPULAR MOVIES TABLE
    public static final class MostPopularMoviesEntry implements BaseColumns {

        private static final String[] COLUMNS = {_ID, COLUMN_MOVIE_ID_KEY};

        private MostPopularMoviesEntry() {
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

        public static final String TABLE_NAME = "most_popular_movies";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_MOST_POPULAR)
                .build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
                + "/" + PATH_MOST_POPULAR;

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " + " );";

    }

    // MY FAVORITES MOVIES TABLE
    public static final class MyFavoritesMoviesEntry implements BaseColumns {

        private static final String[] COLUMNS = {_ID, COLUMN_MOVIE_ID_KEY};

        private MyFavoritesMoviesEntry() {
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

        public static final String TABLE_NAME = "my_favorites_movies";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
                + "/" + PATH_FAVORITES;

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " + " );";
    }


    public static final class ReviewColumns implements BaseColumns {
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String URL = "url";
        public static final String MOVIE_ID = "movie_id";
    }

    public static final class TrailerColumns implements BaseColumns {
        public static final String ISO6391 = "iso6391";
        public static final String ISO31661 = "iso31661";
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String SITE = "site";
        public static final String SIZE = "size";
        public static final String TYPE = "type";
        public static final String MOVIE_ID = "movie_id";
    }

    public static final Uri CONTENT_URI_REVIEWS = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_REVIEWS)
            .build();

    public static final Uri CONTENT_URI_TRAILERS = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_TRAILERS)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }


    public static long getIdFromUri(Uri uri) {
        return ContentUris.parseId(uri);
    }

}
