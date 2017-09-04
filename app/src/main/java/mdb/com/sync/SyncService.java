package mdb.com.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import javax.inject.Inject;
import mdb.com.data.db.MoviesContract;
import mdb.com.data.api.MoviesService;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.reponse.DiscoverAndSearchResponse;
import mdb.com.loaders.MoviesLoader;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SyncService {

    private static final String TAG = SyncService.class.getSimpleName();

    public static final String BROADCAST_UPDATE_FINISHED = "UpdateFinished";
    public static final String EXTRA_IS_SUCCESSFUL_UPDATED = "isSuccessfulUpdated";

    private static final int PAGE_SIZE = 20;
    private static final String LOG_TAG = "MoviesService";

    private volatile boolean loading = false;

    MoviesLoader moviesLoader;
    MoviesService service;
    Context context;


    public static final String ACTION_UPDATE_MOVIE = TAG + ".updateMovie";
    public static final String SORT_TYPE = TAG + ".sortType";

    public static final String EXTRA = TAG + ".contentValues";

    @Inject
    public SyncService(Context context, MoviesLoader moviesLoader, MoviesService service) {
        this.moviesLoader = moviesLoader;
        this.service = service;
        this.context = context;
    }

    public static void updateMovie(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_UPDATE_MOVIE);
        intent.setData(uri);
        intent.putExtra(EXTRA, values);
        context.startService(intent);
    }

    public static void fetch(Context context, Bundle bundle, String action) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(action);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    private void callDiscoverMovies(String sort, @Nullable Integer page) {
        service.discoverMovies(String.valueOf(sort), page)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::clearMoviesSortTableIfNeeded)
                .doOnNext(this::logResponse)
                .map(DiscoverAndSearchResponse::getResults)
                .flatMap(Observable::from)
                .map(this::saveMovie)
                .map(MoviesContract::getIdFromUri)
                .doOnNext(this::saveMovieReference)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        loading = false;
                        sendUpdateFinishedBroadcast(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                        sendUpdateFinishedBroadcast(false);
                        Log.e(LOG_TAG, "Error", e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        // do nothing
                    }
                });
    }

    private void saveMovieReference(Long movieId) {
        ContentValues entry = new ContentValues();
        entry.put(MoviesContract.TrailerColumns.MOVIE_ID, movieId);
        context.getContentResolver().insert(MoviesContract.CONTENT_URI_MOVIES, entry);
    }

    private Uri saveMovie(MovieEntity movie) {
        return context.getContentResolver().insert(MoviesContract.CONTENT_URI_MOVIES, movie.convertToContentValues());
    }

    private void logResponse(DiscoverAndSearchResponse<MovieEntity> discoverMoviesResponse) {
        Log.d(LOG_TAG, "page == " + discoverMoviesResponse.getPage() + " " +
                discoverMoviesResponse.getResults().toString());
    }

    private void clearMoviesSortTableIfNeeded(DiscoverAndSearchResponse<MovieEntity> discoverMoviesResponse) {
        if (discoverMoviesResponse.getPage() == 1) {
            context.getContentResolver().delete(
                    MoviesContract.CONTENT_URI_MOVIES,
                    null,
                    null
            );
        }
    }

    private void sendUpdateFinishedBroadcast(boolean successfulUpdated) {
        Intent intent = new Intent(BROADCAST_UPDATE_FINISHED);
        intent.putExtra(EXTRA_IS_SUCCESSFUL_UPDATED, successfulUpdated);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private int getCurrentPage(Uri uri) {
        Cursor movies = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        int currentPage = 1;
        if (movies != null) {
            currentPage = (movies.getCount() - 1) / PAGE_SIZE + 1;
            movies.close();
        }
        return currentPage;
    }
}
