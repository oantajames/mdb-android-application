package mdb.com.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;
import mdb.com.di.component.PerActivity;
import mdb.com.repository.MoviesRepository;

/**
 * @author james on 7/24/17.
 */
@Module
public class MoviesGridModule {

    @Provides
    @PerActivity
    SharedPreferences providesSharedPreferences(Context application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    public MoviesRepository providesMoviesService(Context context, mdb.com.data.api.MoviesService theMovieDbService) {
        return new MoviesRepository(context, theMovieDbService);
    }

}
