package mdb.com.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;
import mdb.com.di.component.PerActivity;
import mdb.com.sync.MoviesRepository;
import mdb.com.sync.SortHelper;

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
    @PerActivity
    SortHelper providesSortHelper(SharedPreferences sharedPreferences) {
        return new SortHelper(sharedPreferences);
    }

    @Provides
    @PerActivity
    public MoviesRepository providesMoviesService(Context context, mdb.com.data.api.MoviesService theMovieDbService,
                                                  SortHelper sortHelper) {
        return new MoviesRepository(context, sortHelper, theMovieDbService);
    }

}
