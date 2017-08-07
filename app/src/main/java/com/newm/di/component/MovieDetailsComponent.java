package com.newm.di.component;

import com.newm.di.module.MovieDetailsModule;
import com.newm.di.module.MoviesGridModule;
import com.newm.presenter.moviedetails.MovieDetailsPresenter;
import com.newm.view.moviedetails.MovieDetailsActivity;
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
