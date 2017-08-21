package com.newm.presenter.moviedetails;

import android.app.LoaderManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import com.newm.data.api.entity.MovieEntity;
import com.newm.data.api.entity.MovieVideoEntity;
import com.newm.data.api.reponse.MovieVideosResponse;
import com.newm.loaders.MoviesLoader;
import com.newm.util.ImageUtils;
import com.newm.util.loader.Callback;
import com.newm.util.loader.RetrofitLoaderManager;
import com.newm.view.moviedetails.MovieDetailsActivity;
import com.newm.view.moviesgrid.MoviesGridActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * @author james on 8/7/17.
 */

public class MovieDetailsPresenterImpl implements MovieDetailsPresenter,
        ImageUtils.BitmapRequestListener, ImageUtils.PalleteListener, Callback<List<MovieVideoEntity>> {

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
    public void onCreate(Uri imageUrl, PresenterPaletteListener presenterPaletteListener) {
        this.presenterPaletteListener = presenterPaletteListener;
        ImageUtils.getBitmapFromUrl(imageUrl, this, context);
    }

    @Override
    public void getMovieTrailers(LoaderManager loaderManager, String movieId, Delegate delegate) {
        this.delegate = delegate;
        RetrofitLoaderManager.restart(loaderManager,
                MovieDetailsActivity.MOVIE_TRAILERS_LOADER_ID,
                moviesLoader.getMovieTrailers(movieId), this);
    }


    // Movie trailers related
    @Override
    public void onFailure(@Nullable Exception ex, @Nullable String message) {

    }

    @Override
    public void onSuccess(List<MovieVideoEntity> result) {
        delegate.setMovieTrailers(result);
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

    public interface PresenterPaletteListener {
        void onPaletteRetrieved(Palette palette);

        void onPaletteFailure();
    }

    public interface Delegate {
        void setMovieTrailers(List<MovieVideoEntity> result);
    }
}
