package mdb.com.view.moviesgrid;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import mdb.com.data.db.MoviesContract;
import mdb.com.view.moviesgrid.util.AbstractMoviesGridFragment;

/**
 * @author james on 9/8/17.
 */

public class FavoritesGridFragmnet extends AbstractMoviesGridFragment {

    public static FavoritesGridFragmnet create() {
        return new FavoritesGridFragmnet();
    }

    @Override
    @NonNull
    protected Uri getContentUri() {
        return MoviesContract.MyFavoritesMoviesEntry.CONTENT_URI;
    }

    @Override
    protected void onCursorLoaded(Cursor data) {
        getAdapter().changeCursor(data);
    }

    @Override
    protected void onRefreshAction() {
        // do nothing
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onMoviesGridInitialisationFinished() {
        // do nothing
    }
}