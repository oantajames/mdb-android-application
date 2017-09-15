package mdb.com.repository;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import mdb.com.data.api.MoviesService;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieTrailerEntity;
import mdb.com.data.api.entity.MovieReviewEntity;
import mdb.com.data.api.reponse.MovieReviewsResponse;
import mdb.com.data.api.reponse.MovieVideosResponse;
import mdb.com.data.db.MoviesContract;
import mdb.com.util.rx.DisposingObserver;
import mdb.com.view.moviedetails.MovieDetailsActivity;

/**
 * @author james on 9/9/17.
 */

public class MovieDetailsRepository {

    public static final String BROADCAST_ON_COMPLETE_TRAILERS = "trailersCompleted";
    public static final String BROADCAST_ON_COMPLETE_REVIEWS = "reviewsCompleted";

    public static final String SUCCESSFUL_UPDATED = "successfulUpdated";

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    @Inject
    public MoviesService moviesService;

    @Inject
    public Context context;

    @Inject
    public MovieDetailsRepository() {
    }

    public void retrieveTrailers(MovieEntity movieEntity) {
        Cursor cursor = context.getContentResolver().query(
                ContentUris.withAppendedId(MoviesContract.TrailersEntry.CONTENT_URI,
                        movieEntity.getId()), null, null, null, null);
        if (null == cursor || cursor.getCount() == 0) {
            moviesService.getMovieVideos(String.valueOf(movieEntity.getId()))
                    .map(MovieVideosResponse::getTrailers)
                    .doOnNext((trailers) -> saveTrailers(trailers, movieEntity.getId()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new DisposingObserver<List<MovieTrailerEntity>>() {
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(LOG_TAG, e.getMessage());
                            sendOnCompleteBroadcast(false, BROADCAST_ON_COMPLETE_TRAILERS);
                        }

                        @Override
                        public void onComplete() {
                            sendOnCompleteBroadcast(true, BROADCAST_ON_COMPLETE_TRAILERS);
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

    public void retrieveReviews(MovieEntity movieEntity) {
        Cursor cursor = context.getContentResolver().query(
                ContentUris.withAppendedId(MoviesContract.ReviewsEntry.CONTENT_URI, movieEntity.getId()), null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            moviesService.getMovieReviews(String.valueOf(movieEntity.getId()))
                    .map(MovieReviewsResponse::getReviews)
                    .doOnNext((reviews) -> saveReviews(reviews, movieEntity.getId()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new DisposingObserver<List<MovieReviewEntity>>() {
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(LOG_TAG, e.getMessage());
                            sendOnCompleteBroadcast(false, BROADCAST_ON_COMPLETE_REVIEWS);
                        }

                        @Override
                        public void onComplete() {
                            sendOnCompleteBroadcast(true, BROADCAST_ON_COMPLETE_REVIEWS);
                        }
                    });
        }
    }

    private void saveReviews(List<MovieReviewEntity> reviewEntities, int movieId) {
        ContentValues[] reviews = new ContentValues[reviewEntities.size()];
        for (int i = 0; i < reviewEntities.size(); i++) {
            reviews[i] = MovieReviewEntity.convert(reviewEntities.get(i), movieId);
        }
        context.getContentResolver().bulkInsert(MoviesContract.ReviewsEntry.CONTENT_URI, reviews);
    }

    private void sendOnCompleteBroadcast(boolean successfulUpdated, String trailersOrReviews) {
        Intent intent;
        switch (trailersOrReviews) {
            case BROADCAST_ON_COMPLETE_TRAILERS:
                intent = new Intent(BROADCAST_ON_COMPLETE_TRAILERS);
                break;
            case BROADCAST_ON_COMPLETE_REVIEWS:
                intent = new Intent(BROADCAST_ON_COMPLETE_TRAILERS);
                break;
            default:
                intent = new Intent();
        }
        intent.putExtra(SUCCESSFUL_UPDATED, successfulUpdated);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
}
