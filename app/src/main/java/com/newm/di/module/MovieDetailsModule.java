package com.newm.di.module;

import com.newm.di.component.PerActivity;
import com.newm.loaders.MoviesLoader;
import com.newm.presenter.moviedetails.MovieDetailsPresenter;
import com.newm.presenter.moviedetails.MovieDetailsPresenterImpl;
import dagger.Module;
import dagger.Provides;

/**
 * @author james on 8/7/17.
 */

@Module
public class MovieDetailsModule {
    @Provides
    @PerActivity
    public MovieDetailsPresenter provideMovieDetailsPresenter(MoviesLoader moviesLoader) {
        return new MovieDetailsPresenterImpl(moviesLoader);
    }
}
