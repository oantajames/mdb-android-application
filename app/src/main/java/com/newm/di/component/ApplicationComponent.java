package com.newm.di.component;

import android.content.Context;
import com.newm.NewmApplication;
import com.newm.data.api.MoviesService;
import com.newm.di.module.ApplicationModule;
import com.newm.di.module.NetworkModule;
import com.newm.loaders.MoviesLoader;
import com.newm.view.BaseActivity;
import com.squareup.okhttp.OkHttpClient;
import dagger.Component;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author james on 7/17/17.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    Context context();

    MoviesLoader moviesLoader();

    @Named("frescoOkHttpClient")
    OkHttpClient frescoOkHttpClient();

    void inject(NewmApplication newmApplication);

    void inject(BaseActivity baseActivity);
}
