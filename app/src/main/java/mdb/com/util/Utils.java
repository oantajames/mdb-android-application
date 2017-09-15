package mdb.com.util;

import android.net.Uri;
import mdb.com.data.db.MoviesContract;

/**
 * @author james on 9/13/17.
 */

public class Utils {

    public static Uri getSortedMoviesUri(String sortString) {
        Sort sort = Sort.fromString(sortString);
        switch (sort) {
            case MOST_POPULAR:
                return MoviesContract.MostPopularMoviesEntry.CONTENT_URI;
            case TOP_RATED:
                return MoviesContract.TopRatedMoviesEntry.CONTENT_URI;
            case FAVORITES:
                return MoviesContract.MyFavoritesMoviesEntry.CONTENT_URI;
            default:
                throw new IllegalStateException("Unknown sort.");
        }
    }
}
