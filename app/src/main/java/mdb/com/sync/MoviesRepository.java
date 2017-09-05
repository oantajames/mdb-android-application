package mdb.com.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import javax.inject.Inject;

import mdb.com.data.db.MoviesContract;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.reponse.DiscoverAndSearchResponse;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getSimpleName();

    public static final String BROADCAST_UPDATE_FINISHED = "UpdateFinished";
    public static final String EXTRA_IS_SUCCESSFUL_UPDATED = "isSuccessfulUpdated";

    private static final int PAGE_SIZE = 20;
    private static final String LOG_TAG = "MoviesRepository";

    private volatile boolean loading = false;

    private SortHelper sortHelper;

    private mdb.com.data.api.MoviesService service;
    private Context context;


    public static final String ACTION_UPDATE_MOVIE = TAG + ".updateMovie";
    public static final String SORT_TYPE = TAG + ".sortType";

    public static final String EXTRA = TAG + ".contentValues";

    @Inject
    public MoviesRepository(Context context, SortHelper sortHelper, mdb.com.data.api.MoviesService service) {
        this.service = service;
        this.context = context;
        this.sortHelper = sortHelper;
    }

    public void refreshMovies(String sort) {
        if (loading) {
            return;
        }
        loading = true;
        callDiscoverMovies(sort, null);
    }

    public boolean isLoading() {
        return loading;
    }

    public void loadMoreMovies(String sort) {
        if (loading) {
            return;
        }
        loading = true;
        Uri uri = sortHelper.getSortedMoviesUri(sort);
        if (uri == null) {
            return;
        }
        callDiscoverMovies(sort, getCurrentPage(uri) + 1);
    }

    private void callDiscoverMovies(String sort, @Nullable Integer page) {
        service.discoverMovies(sort, page)
                .subscribeOn(Schedulers.newThread())
                .doOnNext((discoverMoviesResponse) -> clearMoviesSortTableIfNeeded(discoverMoviesResponse, sort))
                .doOnNext(this::logResponse)
                .map(DiscoverAndSearchResponse::getResults)
                .flatMap(Observable::from)
                .map(this::saveMovie)
                .map(MoviesContract::getIdFromUri)
                .doOnNext((movieId) -> saveMovieReference(movieId, sort))
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

    private void saveMovieReference(Long movieId, String sort) {
        ContentValues entry = new ContentValues();
        entry.put(MoviesContract.COLUMN_MOVIE_ID_KEY, movieId);
        context.getContentResolver().insert(sortHelper.getSortedMoviesUri(sort), entry);
    }

    private Uri saveMovie(MovieEntity movie) {
        return context.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, movie.convertToContentValues());
    }

    private void logResponse(DiscoverAndSearchResponse<MovieEntity> discoverMoviesResponse) {
        Log.d(LOG_TAG, "page == " + discoverMoviesResponse.getPage() + " " +
                discoverMoviesResponse.getResults().toString());
    }

    private void clearMoviesSortTableIfNeeded(DiscoverAndSearchResponse<MovieEntity> discoverMoviesResponse, String sort) {
        if (discoverMoviesResponse.getPage() == 1) {
            context.getContentResolver().delete(
                    sortHelper.getSortedMoviesUri(sort),
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
