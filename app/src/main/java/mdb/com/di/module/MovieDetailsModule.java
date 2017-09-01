package mdb.com.di.module;

import mdb.com.di.component.PerActivity;
import mdb.com.loaders.MoviesLoader;
import mdb.com.presenter.moviedetails.MovieDetailsPresenter;
import mdb.com.presenter.moviedetails.MovieDetailsPresenterImpl;
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
