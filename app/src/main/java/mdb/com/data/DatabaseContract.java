package mdb.com.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author james on 9/1/17.
 */

public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "mdb.com.data";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_MOST_POPULAR = "most_popular";
    public static final String PATH_FAVORITES = "favorites";

    //Tables
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_REVIEWS = "reviews";
    public static final String TABLE_TRAILERS = "trailers";

    public static final class MovieColumns implements BaseColumns {
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
        public static final String IS_FAVORITE = "is_favorite";
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


    private static final String MOST_POPULAR_SORT = String.format("%s ASC, %s DESC, %s ASC",
            MovieColumns.VOTE_AVERAGE, MovieColumns.POPULARITY, MovieColumns.IS_FAVORITE);

    private static final String TOP_RATED_SORT = String.format("%s DESC, %s ASC, %s ASC",
            MovieColumns.VOTE_AVERAGE, MovieColumns.POPULARITY, MovieColumns.IS_FAVORITE);

    private static final String FAVORITE_SORT = String.format("%s DESC, %s ASC, %s ASC",
            MovieColumns.IS_FAVORITE, MovieColumns.VOTE_AVERAGE, MovieColumns.POPULARITY);

    public static final Uri CONTENT_URI_MOVIES = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIES)
            .build();

    public static final Uri CONTENT_URI_REVIEWS = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_REVIEWS)
            .build();

    public static final Uri CONTENT_URI_TRAILERS = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_TRAILERS)
            .build();


    public static String getSort(@SortType int type) {
        switch (type) {
            case SortType.FAVORITES:
                return FAVORITE_SORT;
            case SortType.MOST_POPULAR:
                return MOST_POPULAR_SORT;
            case SortType.NOT_DEFINED:
            case SortType.TOP_RATED:
                return TOP_RATED_SORT;
            default:
                return null;
        }
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


}
