package mdb.com.view.moviedetails;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import mdb.com.data.api.MoviesService;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieVideoEntity;
import mdb.com.data.api.entity.ReviewEntity;
import mdb.com.data.api.reponse.DiscoverAndSearchResponse;
import mdb.com.data.api.reponse.MovieReviewsResponse;
import mdb.com.data.api.reponse.MovieVideosResponse;
import mdb.com.data.db.MoviesContract;
import mdb.com.sync.SortHelper;
import mdb.com.view.BaseRepository;

/**
 * @author james on 9/9/17.
 */

public class MovieDetailsRepository extends BaseRepository {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    @Inject
    MoviesService moviesService;
    @Inject
    Context context;

    @Inject
    public MovieDetailsRepository() {
    }

    public void loadMovieVideos(MovieEntity movieEntity) {
        moviesService.getMovieVideos(String.valueOf(movieEntity.getId()))
                .compose(getLifecycleProvider().bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .map(MovieVideosResponse::getTrailers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MovieVideoEntity>>() {

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<MovieVideoEntity> movieVideos) {
//                        movieTrailersAdapter.setMovieVideos(movieVideos);
                    }
                });
    }

    //do I  needed this for each trailer ? // TODO: 9/9/17
    private void saveTrailerReference(Long movieId) {
        ContentValues entry = new ContentValues();
        entry.put(MoviesContract.COLUMN_MOVIE_ID_KEY, movieId);
        context.getContentResolver().insert(MoviesContract.TrailersEntry.CONTENT_URI, entry);
    }

    private Uri saveTrailer(MovieEntity movie) {
        return context.getContentResolver().insert(MoviesContract.TrailersEntry.CONTENT_URI, movie.convertToContentValues());
    }

    private void logResponse(DiscoverAndSearchResponse<MovieEntity> discoverMoviesResponse) {
        Log.d(LOG_TAG, "page == " + discoverMoviesResponse.getPage() + " " +
                discoverMoviesResponse.getResults().toString());
    }

    //todo save movies
    public void loadMovieReviews(MovieEntity movieEntity) {
        moviesService.getMovieReviews(String.valueOf(movieEntity.getId()))
                .compose(getLifecycleProvider().bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .map(MovieReviewsResponse::getReviews)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ReviewEntity>>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<ReviewEntity> movieReviews) {
//                        reviewsAdapter.setReviews(movieReviews);
                    }
                });
    }


}
