package com.newm.di.component;

import com.newm.di.module.MoviesGridModule;
import com.newm.presenter.moviesgrid.MoviesGridPresenter;
import com.newm.view.moviesgrid.FragmentMoviesList;
import com.newm.view.moviesgrid.MoviesGridActivity;
import dagger.Component;


/**
 * @author james on 7/24/17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MoviesGridModule.class)
public interface MoviesGridComponent {

    //what
    MoviesGridPresenter providePresenter();

    //where
    void inject(MoviesGridActivity moviesGridActivity);

    void inject(FragmentMoviesList fragmentMoviesList);

}
