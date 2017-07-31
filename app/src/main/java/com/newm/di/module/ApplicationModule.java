package com.newm.di.module;

import android.app.Application;
import android.content.Context;
import com.newm.NewmApplication;
import com.newm.di.component.ApplicationComponent;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * @author james on 7/18/17.
 */

@Module
public class ApplicationModule {

    private final NewmApplication application;

    public ApplicationModule(NewmApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public NewmApplication provideNewmApplication() {
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
