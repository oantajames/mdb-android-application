package mdb.com.presenter.moviedetails;

import android.app.LoaderManager;
import android.net.Uri;

/**
 * @author james on 8/7/17.
 */

public interface MovieDetailsPresenter {

    void onCreate(Uri imageUrl, MovieDetailsPresenterImpl.PresenterPaletteListener presenterPaletteListener, MovieDetailsPresenterImpl.Delegate delegate);

    void getMovieTrailers(LoaderManager loaderManager, String movieId);

    void getMovieReviews(LoaderManager loaderManager, String movieId);


}
