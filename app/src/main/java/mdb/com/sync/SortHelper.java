package mdb.com.sync;

import android.net.Uri;

import javax.inject.Inject;
import mdb.com.data.db.MoviesContract;

public class SortHelper {

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
