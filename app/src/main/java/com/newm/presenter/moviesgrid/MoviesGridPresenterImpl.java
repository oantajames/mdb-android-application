package com.newm.presenter.moviesgrid;

import android.app.LoaderManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.newm.data.api.entity.MovieEntity;
import com.newm.loaders.MoviesLoader;
import com.newm.util.loader.Callback;
import com.newm.util.loader.RetrofitLoaderManager;
import com.newm.view.moviesgrid.MoviesGridActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * @author james on 7/18/17.
 */
@SuppressWarnings("WeakerAccess")
public class MoviesGridPresenterImpl implements MoviesGridPresenter {

    private static final String TAG = MoviesGridPresenterImpl.class.getSimpleName();

    MoviesLoader moviesLoader;

    @Inject
    public MoviesGridPresenterImpl(MoviesLoader moviesLoader) {
        this.moviesLoader = moviesLoader;
    }

    @Override
    public void getPopularMovies(LoaderManager loaderManager, Delegate delegate, int loaderId) {
        RetrofitLoaderManager.restart(loaderManager, loaderId, moviesLoader.getPopularMovies(), new Callback<List<MovieEntity>>() {
            @Override
            public void onFailure(@Nullable Exception ex, @Nullable String message) {
                Log.e(TAG, message);
            }

            @Override
            public void onSuccess(List<MovieEntity> result) {
                delegate.setMoviesList(result);
            }
        });
    }

    @Override
    public void getTopRatedMovies(LoaderManager loaderManager, Delegate delegate, int loaderId) {
        RetrofitLoaderManager.restart(loaderManager, loaderId, moviesLoader.getTopRatedMovies(), new Callback<List<MovieEntity>>() {
            @Override
            public void onFailure(@Nullable Exception ex, @Nullable String message) {
                Log.e(TAG, message);
            }

            @Override
            public void onSuccess(List<MovieEntity> result) {
                delegate.setMoviesList(result);
            }
        });
    }


    public interface Delegate {
        void setMoviesList(List<MovieEntity> result);
    }
}
