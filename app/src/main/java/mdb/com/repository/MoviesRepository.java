package mdb.com.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import mdb.com.data.api.MoviesService;
import mdb.com.data.db.MoviesContract;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.reponse.DiscoverAndSearchResponse;
import mdb.com.util.DisposingObserver;
import mdb.com.util.Utils;
import mdb.com.view.BaseRepository;


public class MoviesRepository extends BaseRepository {

    public static final String BROADCAST_UPDATE_FINISHED = "UpdateFinished";
    public static final String EXTRA_IS_SUCCESSFUL_UPDATED = "isSuccessfulUpdated";

    private static final int PAGE_SIZE = 20;
    private static final String LOG_TAG = "MoviesRepository";

    private volatile boolean loading = false;

    private MoviesService service;
    private Context context;

    @Inject
    public MoviesRepository(Context context, MoviesService service) {
        this.service = service;
        this.context = context;
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
        Uri uri = Utils.getSortedMoviesUri(sort);
        if (uri == null) {
            return;
        }
        callDiscoverMovies(sort, getCurrentPage(uri) + 1);
    }

    private void callDiscoverMovies(String sort, @Nullable Integer page) {
        service.discoverMovies(sort, page)
                .compose(getLifecycleProvider().bindToLifecycle())
                .doOnNext((discoverMoviesResponse) -> clearMoviesSortTableIfNeeded(discoverMoviesResponse, sort))
                .doOnNext(this::logResponse)
                .map(DiscoverAndSearchResponse::getResults)
                .flatMapIterable(movieEntities -> movieEntities)
                .map(this::saveMovie)
                .map(MoviesContract::getIdFromUri)
                .doOnNext((movieId) -> saveMovieReference(movieId, sort))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DisposingObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loading = false;
                        Log.e(LOG_TAG, "Error", e);
                    }

                    @Override
                    public void onComplete() {
                        loading = false;
                        sendUpdateFinishedBroadcast(true);
                    }
                });
    }

    private void saveMovieReference(Long movieId, String sort) {
        ContentValues entry = new ContentValues();
        entry.put(MoviesContract.COLUMN_MOVIE_ID_KEY, movieId);
        context.getContentResolver().insert(Utils.getSortedMoviesUri(sort), entry);
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
                    Utils.getSortedMoviesUri(sort),
                    null,
                    null
            );
        }
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

    private void sendUpdateFinishedBroadcast(boolean successfulUpdated) {
        Intent intent = new Intent(BROADCAST_UPDATE_FINISHED);
        intent.putExtra(EXTRA_IS_SUCCESSFUL_UPDATED, successfulUpdated);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
