package com.newm.di.component;

import android.content.Context;
import com.newm.di.module.MoviesGridModule;
import com.newm.loaders.MoviesLoader;
import com.newm.presenter.MoviesGridPresenter;
import com.newm.view.mostpopular.MoviesGridActivity;
import dagger.Component;
import javax.inject.Singleton;


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

}
