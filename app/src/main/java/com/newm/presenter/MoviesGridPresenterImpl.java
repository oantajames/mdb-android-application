package com.newm.presenter;

import android.app.LoaderManager;
import android.util.Log;
import com.newm.data.api.MovieEntity;
import com.newm.loaders.MoviesLoader;
import com.newm.util.loader.Callback;
import com.newm.util.loader.RetrofitLoaderManager;
import com.newm.view.mostpopular.MoviesGridActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * @author james on 7/18/17.
 */
public class MoviesGridPresenterImpl implements MoviesGridPresenter, Callback<List<MovieEntity>> {

    private static final String TAG = MoviesGridPresenterImpl.class.getSimpleName();

    private Delegate delegate;

    MoviesLoader moviesLoader;

    @Inject
    public MoviesGridPresenterImpl(MoviesLoader moviesLoader) {
        this.moviesLoader = moviesLoader;
    }

    @Override
    public void getPopularMovies(LoaderManager loaderManager, Delegate delegate) {
        this.delegate = delegate;
        //get first the popular movies
        RetrofitLoaderManager.restart(loaderManager, MoviesGridActivity.MOVIE_LOADER_ID, moviesLoader.getPopularMovies(), this);
    }

    @Override
    public void getTopRatedMovies(LoaderManager loaderManager, Delegate delegate) {
        RetrofitLoaderManager.restart(loaderManager, MoviesGridActivity.MOVIE_LOADER_ID, moviesLoader.getTopRatedMovies(), this);
    }


    @Override
    public void onFailure(Exception ex) {
        Log.e(TAG, ex.getMessage());
    }

    @Override
    public void onSuccess(List<MovieEntity> result) {
        delegate.setMoviesList(result);
    }

    public interface Delegate {
        void setMoviesList(List<MovieEntity> result);
    }
}
