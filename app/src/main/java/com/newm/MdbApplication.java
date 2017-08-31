package com.newm;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.newm.di.component.ApplicationComponent;
import com.newm.di.component.DaggerApplicationComponent;
import com.newm.di.module.ApplicationModule;

import com.newm.di.module.NetworkModule;
import com.squareup.okhttp.OkHttpClient;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author james on 7/17/17.
 */

public class MdbApplication extends Application {

    //TODO : You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star).

    //TODO : You'll create a database and content provider to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).

    //TODO : You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

    private ApplicationComponent applicationComponent;

    @Inject
    @Named("frescoOkHttpClient")
    OkHttpClient frescoOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .build();
        applicationComponent.inject(this);

        Fresco.initialize(getApplicationContext(),
                OkHttpImagePipelineConfigFactory.newBuilder(getApplicationContext(), frescoOkHttpClient)
                        .setDownsampleEnabled(true)
                        .build());
    }

    public boolean isNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
