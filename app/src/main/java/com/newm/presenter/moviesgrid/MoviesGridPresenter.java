package com.newm.presenter.moviesgrid;

import android.app.LoaderManager;

/**
 * @author james on 7/18/17.
 */
public interface MoviesGridPresenter {
    void getPopularMovies(LoaderManager loaderManager, MoviesGridPresenterImpl.Delegate delegate);

    void getTopRatedMovies(LoaderManager loaderManager, MoviesGridPresenterImpl.Delegate delegate);
}
