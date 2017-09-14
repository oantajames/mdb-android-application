package mdb.com;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import mdb.com.di.component.ApplicationComponent;
import mdb.com.di.component.DaggerApplicationComponent;
import mdb.com.di.module.ApplicationModule;

import mdb.com.di.module.NetworkModule;
import com.squareup.okhttp.OkHttpClient;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author james on 7/17/17.
 */

public class MdbApplication extends Application {
    //todo - add adaptive icon
    //todo - add transitions
    // todo - add recyclerview animations

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
