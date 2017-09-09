package mdb.com.di.module;

import mdb.com.BuildConfig;
import mdb.com.data.api.ApiConstants;
import mdb.com.data.api.MoviesService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
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
    public Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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

}
