package mdb.com.di.module;

import mdb.com.di.component.PerActivity;
import mdb.com.loaders.MoviesLoader;
import mdb.com.presenter.moviesgrid.MoviesGridPresenter;
import mdb.com.presenter.moviesgrid.MoviesGridPresenterImpl;
import dagger.Module;
import dagger.Provides;

/**
 * @author james on 7/24/17.
 */
@Module
public class MoviesGridModule {

    @Provides
    @PerActivity
    public MoviesGridPresenter provideMoviesGridPresenter(MoviesLoader moviesLoader) {
        return new MoviesGridPresenterImpl(moviesLoader);
    }

}
