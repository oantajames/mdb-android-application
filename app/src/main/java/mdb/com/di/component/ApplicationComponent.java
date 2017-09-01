package mdb.com.di.component;

import android.content.Context;
import mdb.com.MdbApplication;
import mdb.com.di.module.ApplicationModule;
import mdb.com.di.module.NetworkModule;
import mdb.com.loaders.MoviesLoader;
import mdb.com.view.BaseActivity;
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

    void inject(MdbApplication mdbApplication);

    void inject(BaseActivity baseActivity);
}
