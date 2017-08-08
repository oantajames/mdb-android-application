package com.newm.di.module;

import android.content.Context;
import com.newm.BuildConfig;
import com.newm.data.api.ApiConstants;
import com.newm.data.api.MoviesService;
import com.newm.loaders.MoviesLoader;
import com.newm.util.loader.MoviesInteractor;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author james on 7/17/17.
 */

@Module(includes = ApplicationModule.class)
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClinet() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalUrl = original.url();
            HttpUrl newUrl = originalUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();
            Request.Builder reqBuilder = original.newBuilder()
                    .url(newUrl);
            Request request = reqBuilder.build();
            return chain.proceed(request);
        });
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("frescoOkHttpClient")
    com.squareup.okhttp.OkHttpClient provideFrescoOkHttpClient() {
        com.squareup.okhttp.OkHttpClient frescoClient = new com.squareup.okhttp.OkHttpClient();
        frescoClient.interceptors().add(chain -> {
            com.squareup.okhttp.Request newRequest = chain.request().newBuilder()
                    .build();
            return chain.proceed(newRequest);
        });
        return frescoClient;
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(OkHttpClient okHttpClient, Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(converterFactory)
                .baseUrl(ApiConstants.BASE_API)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public Converter.Factory provideConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public MoviesService provideMoviesService(Retrofit retrofit) {
        return retrofit.create(MoviesService.class);
    }

    @Provides
    @Singleton
    public MoviesLoader provideMoviesLoader(Context context, MoviesService service, MoviesInteractor interactor) {
        return new MoviesLoader(context, service, interactor);
    }

    @Provides
    @Singleton
    public MoviesInteractor provideMoviesInteractor() {
        return new MoviesInteractor();
    }

}
