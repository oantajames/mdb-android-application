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

    static final String CONTENT_AUTHORITY = "mdb.com.data";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_MOVIES = "movies";
    public static final String PATH_TOP_RATED = "top_rated";
    static final String PATH_MOST_POPULAR = "most_popular";
    static final String PATH_FAVORITES = "favorites";
    public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

    static final String PATH_REVIEWS = "reviews";
    static final String PATH_TRAILERS = "trailers";

    //Tables
    static final String TABLE_MOVIES = "movies";
    static final String TABLE_REVIEWS = "reviews";
    static final String TABLE_TRAILERS = "trailers";

    public static final class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        static final String CONTENT_ITEM_TYPE =
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

        static final String[] COLUMNS = {_ID, BACKDROP_PATH, HAS_VIDEO, IS_ADULT,
                ORIGINAL_LANGUAGE, ORIGINAL_TITLE, OVERVIEW, POPULARITY, POSTER_PATH,
                RELEASE_DATE, TITLE, VOTE_AVERAGE, VOTE_COUNT};

        static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        static String[] getColumns() {
            return COLUMNS.clone();
        }

        static final String SQL_CREATE_TABLE_MOVIES = String.format("CREATE TABLE %s"
                        + " (%s INTEGER PRIMARY KEY, %s TEXT, %s BOOLEAN, %s BOOLEAN, %s TEXT, %s TEXT,"
                        + "%s TEXT, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT, %s DOUBLE, %s INTEGER )",
                TABLE_MOVIES, _ID, BACKDROP_PATH, HAS_VIDEO, IS_ADULT, ORIGINAL_LANGUAGE, ORIGINAL_TITLE,
                OVERVIEW, POPULARITY, POSTER_PATH, RELEASE_DATE, TITLE, VOTE_AVERAGE, VOTE_COUNT);
    }

    // TOP RATED MOVIES TABLE
    public static final class TopRatedMoviesEntry implements BaseColumns {

        private static final String[] COLUMNS = {_ID, COLUMN_MOVIE_ID_KEY};

        private TopRatedMoviesEntry() {
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

        static final String TABLE_NAME = "top_rated_movies";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED)
                .build();

        static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
                + "/" + PATH_TOP_RATED;

        static final String SQL_CREATE_TABLE =
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

        static final String TABLE_NAME = "most_popular_movies";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_MOST_POPULAR)
                .build();

        static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
                + "/" + PATH_MOST_POPULAR;

        static final String SQL_CREATE_TABLE =
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

        static final String TABLE_NAME = "favorites";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
                + "/" + PATH_FAVORITES;

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " + " );";
    }


    public static final class ReviewsEntry implements BaseColumns {

        static final String TABLE_NAME = "reviews";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();

        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String URL = "url";
        public static final String MOVIE_ID = "movie_id";

        static final String SQL_CREATE_TABLE_REVIEWS = String.format("CREATE TABLE %s"
                        + " (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)",
                TABLE_REVIEWS, _ID, AUTHOR, CONTENT, MOVIE_ID, URL);
    }

    public static final class TrailersEntry implements BaseColumns {

        static final String TABLE_NAME = "trailers";

        public static final Uri CONTENT_URI = MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILERS)
                .build();

        public static final String ISO6391 = "iso6391";
        public static final String ISO31661 = "iso31661";
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String SITE = "site";
        public static final String SIZE = "size";
        public static final String TYPE = "type";
        public static final String MOVIE_ID = "movie_id";

        static final String SQL_CREATE_TABLE_TRAILERS = String.format("CREATE TABLE %s"
                        + " (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT,  %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT)",
                TABLE_TRAILERS, _ID, ISO6391, ISO31661, KEY, NAME, MOVIE_ID, SITE, SIZE, TYPE);
    }

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
