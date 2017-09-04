package mdb.com.sync;

import android.content.SharedPreferences;
import android.net.Uri;
import javax.inject.Inject;
import mdb.com.data.db.MoviesContract;

public final class SortHelper {

    private static final String PREF_SORT_BY_KEY = "sortBy";
    private static final String PREF_SORT_BY_DEFAULT_VALUE = "popularity.desc";

    private SharedPreferences sharedPreferences;

    @Inject
    public SortHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Sort getSortByPreference() {
        String sort = sharedPreferences.getString(
                PREF_SORT_BY_KEY,
                PREF_SORT_BY_DEFAULT_VALUE
        );
        return Sort.fromString(sort);
    }

    public Uri getSortedMoviesUri() {
        Sort sort = getSortByPreference();
        switch (sort) {
            case MOST_POPULAR:
                return MoviesContract.MostPopularMoviesEntry.CONTENT_URI;
            case TOP_RATED:
                return MoviesContract.TopRatedMoviesEntry.CONTENT_URI;
            default:
                throw new IllegalStateException("Unknown sort.");
        }
    }

    public boolean saveSortByPreference(Sort sort) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(
                PREF_SORT_BY_KEY,
                sort.toString()
        );
        return editor.commit();
    }
}
