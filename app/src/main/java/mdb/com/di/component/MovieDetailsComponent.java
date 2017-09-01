package mdb.com.di.component;

import mdb.com.di.module.MovieDetailsModule;
import mdb.com.presenter.moviedetails.MovieDetailsPresenter;
import mdb.com.view.moviedetails.MovieDetailsActivity;
import dagger.Component;

/**
 * @author james on 8/7/17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MovieDetailsModule.class)
public interface MovieDetailsComponent {
    // what
    MovieDetailsPresenter movieDetailsPresenter();

    // where
    void inject(MovieDetailsActivity movieDetailsActivity);

}
