package mdb.com.repository;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import mdb.com.data.api.MoviesService;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieTrailerEntity;
import mdb.com.data.api.entity.ReviewEntity;
import mdb.com.data.api.reponse.QueryMoviesResponse;
import mdb.com.data.api.reponse.MovieReviewsResponse;
import mdb.com.data.api.reponse.MovieVideosResponse;
import mdb.com.data.db.MoviesContract;
import mdb.com.util.rx.DisposingObserver;
import mdb.com.view.moviedetails.MovieDetailsActivity;

/**
 * @author james on 9/9/17.
 */

public class MovieDetailsRepository {

    public static final String BROADCAST_ON_COMPLETE = "updateFinished";
    public static final String SUCCESSFUL_UPDATED = "successfulUpdated";

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    @Inject
    public MoviesService moviesService;
    @Inject
    public Context context;

    @Inject
    public MovieDetailsRepository() {
    }

    public void loadVideos(MovieEntity movieEntity) {
        Cursor cursor = context.getContentResolver().query(
                ContentUris.withAppendedId(MoviesContract.TrailersEntry.CONTENT_URI,
                        movieEntity.getId()), null, null, null, null);
        if (null == cursor || cursor.getCount() == 0) {
            moviesService.getMovieVideos(String.valueOf(movieEntity.getId()))
                    .map(MovieVideosResponse::getTrailers)
                    .doOnNext((movie) -> saveTrailers(movie, movieEntity.getId()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new DisposingObserver<List<MovieTrailerEntity>>() {
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            sendOnCompleteBroadcast(true);
                        }
                    });
        } else {
            cursor.close();
        }
    }

    private void saveTrailers(List<MovieTrailerEntity> trailersList, int movieId) {
        ContentValues[] trailers = new ContentValues[trailersList.size()];
        for (int i = 0; i < trailersList.size(); i++) {
            trailers[i] = MovieTrailerEntity.convert(trailersList.get(i), movieId);
        }
        context.getContentResolver().bulkInsert(MoviesContract.TrailersEntry.CONTENT_URI, trailers);
    }

    private void logResponse(QueryMoviesResponse<MovieEntity> discoverMoviesResponse) {
        Log.d(LOG_TAG, "page == " + discoverMoviesResponse.getPage() + " " +
                discoverMoviesResponse.getResults().toString());
    }

    public void loadMovieReviews(MovieEntity movieEntity) {
        //TODO - QUERY THE DB FIRST THEN REQUEST
        moviesService.getMovieReviews(String.valueOf(movieEntity.getId()))
                .subscribeOn(Schedulers.newThread())
                .map(MovieReviewsResponse::getReviews)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposingObserver<List<ReviewEntity>>() {
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull List<ReviewEntity> reviewEntities) {
                        // reviewsAdapter.setReviews(movieReviews);
                    }
                });
    }

    private void sendOnCompleteBroadcast(boolean successfulUpdated) {
        Intent intent = new Intent(BROADCAST_ON_COMPLETE);
        intent.putExtra(SUCCESSFUL_UPDATED, successfulUpdated);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
