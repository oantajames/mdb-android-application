package mdb.com.di.component;

import android.app.Application;

import mdb.com.di.module.MoviesGridModule;
import mdb.com.view.moviesgrid.FragmentMoviesList;
import mdb.com.view.moviesgrid.MoviesGridActivity;
import dagger.Component;


/**
 * @author james on 7/24/17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {MoviesGridModule.class})
public interface MoviesGridComponent {

    //where
    void inject(MoviesGridActivity moviesGridActivity);

    void inject(FragmentMoviesList fragmentMoviesList);

}
