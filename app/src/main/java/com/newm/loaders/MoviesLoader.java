package com.newm.loaders;

import android.content.Context;
import com.newm.data.api.entity.MovieEntity;
import com.newm.data.api.MoviesService;
import com.newm.data.api.entity.MovieVideoEntity;
import com.newm.data.api.entity.ReviewEntity;
import com.newm.util.loader.MoviesInteractor;
import com.newm.util.loader.RetrofitLoader;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author james on 7/20/17.
 */
@SuppressWarnings("WeakerAccess")
@Singleton
public class MoviesLoader {

    Context context;
    MoviesService service;
    MoviesInteractor interactor;

    @Inject
    public MoviesLoader(Context context, MoviesService service, MoviesInteractor interactor) {
        this.context = context;
        this.service = service;
        this.interactor = interactor;
    }

    public RetrofitLoader getPopularMovies() {
        return new RetrofitLoader<List<MovieEntity>, MoviesService>(context, service) {
            @Override
            public List<MovieEntity> call(MoviesService service) {
                return interactor.createPopularMoviesCall(service);
            }
        };
    }

    public RetrofitLoader getTopRatedMovies() {
        return new RetrofitLoader<List<MovieEntity>, MoviesService>(context, service) {

            @Override
            public List<MovieEntity> call(MoviesService service) {
                return interactor.createTopRatedMoviesCall(service);
            }
        };
    }

    public RetrofitLoader getMovieTrailers(final String movieId) {
        return new RetrofitLoader<List<MovieVideoEntity>, MoviesService>(context, service) {

            @Override
            public List<MovieVideoEntity> call(MoviesService service) {
                return interactor.createMovieTrailersCall(service, movieId);
            }
        };
    }

    public RetrofitLoader getMovieReviews(final String movieId) {
        return new RetrofitLoader<List<ReviewEntity>, MoviesService>(context, service) {

            @Override
            public List<ReviewEntity> call(MoviesService service) {
                return interactor.createMovieReviewsCall(service, movieId);
            }
        };
    }


}
