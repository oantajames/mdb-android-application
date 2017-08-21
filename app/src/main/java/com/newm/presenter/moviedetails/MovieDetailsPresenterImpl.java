package com.newm.presenter.moviedetails;

import android.app.LoaderManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import com.newm.data.api.entity.MovieVideoEntity;
import com.newm.data.api.entity.ReviewEntity;
import com.newm.loaders.MoviesLoader;
import com.newm.util.ImageUtils;
import com.newm.util.loader.Callback;
import com.newm.util.loader.RetrofitLoaderManager;
import com.newm.view.moviedetails.MovieDetailsActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * @author james on 8/7/17.
 */

public class MovieDetailsPresenterImpl implements MovieDetailsPresenter,
        ImageUtils.BitmapRequestListener, ImageUtils.PalleteListener {

    @Inject
    Context context;

    private Delegate delegate;

    private PresenterPaletteListener presenterPaletteListener;

    private MoviesLoader moviesLoader;

    @Inject
    public MovieDetailsPresenterImpl(MoviesLoader moviesLoader) {
        this.moviesLoader = moviesLoader;
    }

    @Override
    public void onCreate(Uri imageUrl, PresenterPaletteListener presenterPaletteListener, MovieDetailsPresenterImpl.Delegate delegate) {
        this.delegate = delegate;
        this.presenterPaletteListener = presenterPaletteListener;
        ImageUtils.getBitmapFromUrl(imageUrl, this, context);
    }

    @Override
    public void getMovieTrailers(LoaderManager loaderManager, String movieId) {
        RetrofitLoaderManager.restart(loaderManager,
                MovieDetailsActivity.MOVIE_TRAILERS_LOADER_ID,
                moviesLoader.getMovieTrailers(movieId), createTrailersListener());
    }

    @Override
    public void getMovieReviews(LoaderManager loaderManager, String movieId) {
        RetrofitLoaderManager.restart(loaderManager,
                MovieDetailsActivity.MOVIE_REVIEWS_LOADER_ID,
                moviesLoader.getMovieReviews(movieId), createReviewsListener());
    }


    //Palette related
    @Override
    public void returnBitmap(Bitmap bitmap) {
        ImageUtils.getPrimaryImageColorsAsync(bitmap, this);
    }

    @Override
    public void bitmapFailure() {
        presenterPaletteListener.onPaletteFailure();
    }

    @Override
    public void onColorGenerate(Palette palette) {
        presenterPaletteListener.onPaletteRetrieved(palette);
    }

    private Callback<List<MovieVideoEntity>> createTrailersListener() {
        return new Callback<List<MovieVideoEntity>>() {
            @Override
            public void onFailure(@Nullable Exception ex, @Nullable String message) {

            }

            @Override
            public void onSuccess(List<MovieVideoEntity> result) {
                delegate.setMovieTrailers(result);
            }
        };
    }

    private Callback<List<ReviewEntity>> createReviewsListener() {
        return new Callback<List<ReviewEntity>>() {
            @Override
            public void onFailure(@Nullable Exception ex, @Nullable String message) {

            }

            @Override
            public void onSuccess(List<ReviewEntity> result) {
                delegate.setMovieReviews(result);
            }
        };
    }

    public interface PresenterPaletteListener {
        void onPaletteRetrieved(Palette palette);

        void onPaletteFailure();
    }

    public interface Delegate {
        void setMovieTrailers(List<MovieVideoEntity> result);

        void setMovieReviews(List<ReviewEntity> reviews);
    }
}
