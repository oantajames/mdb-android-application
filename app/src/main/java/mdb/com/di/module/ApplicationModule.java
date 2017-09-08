package mdb.com.di.module;

import android.app.Application;
import android.content.Context;
import mdb.com.MdbApplication;
import mdb.com.di.component.ApplicationComponent;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * @author james on 7/18/17.
 */

@Module
public class ApplicationModule {

    private final MdbApplication application;

    public ApplicationModule(MdbApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public MdbApplication provideNewmApplication() {
        return application;
    }

    @Singleton
    @Provides
    public Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public ApplicationComponent provideApplicationComponent() {
        return application.getApplicationComponent();
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return this.application;
    }


}
