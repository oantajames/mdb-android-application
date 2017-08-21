package com.newm.presenter.moviedetails;

import android.app.LoaderManager;
import android.net.Uri;
import android.support.v7.graphics.Palette;

/**
 * @author james on 8/7/17.
 */

public interface MovieDetailsPresenter {

    void onCreate(Uri imageUrl, MovieDetailsPresenterImpl.PresenterPaletteListener presenterPaletteListener);

    void getMovieTrailers(LoaderManager loaderManager, String movieId, MovieDetailsPresenterImpl.Delegate delegate);

}
