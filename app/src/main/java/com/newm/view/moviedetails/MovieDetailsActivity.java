package com.newm.view.moviedetails;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.newm.R;
import com.newm.data.api.MovieEntity;
import com.newm.di.component.DaggerMovieDetailsComponent;
import com.newm.di.module.MovieDetailsModule;
import com.newm.presenter.moviedetails.MovieDetailsPresenter;
import com.newm.presenter.moviedetails.MovieDetailsPresenterImpl;
import com.newm.util.SharedPreferencesUtil;
import com.newm.view.BaseActivity;
import com.newm.view.moviesgrid.MoviesGridActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

/**
 * @author james on 8/1/17.
 */

public class MovieDetailsActivity extends BaseActivity implements MovieDetailsPresenterImpl.PresenterPaletteListener {

    public static final String EXTRA_MOVIE_IMAGE_TRANSITION = "movieImageTransition";

    @Inject
    MovieDetailsPresenter presenter;
    //"w92", "w154", "w185", "w342", "w500", "w780", or "original"
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BACKDROP_SIZE = "original";
    private static final String POSTER_SIZE = "w185";

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @Bind(R.id.header)
    SimpleDraweeView movieHeader;
    @Bind(R.id.backdrop_gradient_view)
    ImageView backdropGradient;
    @Bind(R.id.movie_release_year)
    TextView movieReleaseYear;
    @Bind(R.id.movie_rating)
    TextView movieRating;
    @Bind(R.id.movie_title)
    TextView movieTitle;
    @Bind(R.id.movie_poster)
    ImageView moviePoster;
    @Bind(R.id.movie_description)
    TextView movieDescription;
    @Bind(R.id.main_background)
    LinearLayout mainBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMovieDetailsComponent movieDetailsComponent = (DaggerMovieDetailsComponent) DaggerMovieDetailsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .movieDetailsModule(new MovieDetailsModule())
                .build();
        movieDetailsComponent.inject(this);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        MovieEntity movieEntity = getIntent().getExtras().getParcelable(MoviesGridActivity.MOVIE_ENTITY);
        if (movieEntity != null) {
            presenter.onCreate(Uri.parse(BASE_IMAGE_URL + BACKDROP_SIZE + movieEntity.getBackdropPath()), this);
        }
        bindViews(movieEntity);
    }

    private void setMoviePosterTransition() {
        Bundle extras = getIntent().getExtras();
        final String transition;
        if (extras != null) {
            transition = extras.getString(EXTRA_MOVIE_IMAGE_TRANSITION);
            moviePoster.setTransitionName(transition);
        }
    }

    private void setBackdropGradient(int colorCode) {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{colorCode, Color.TRANSPARENT});
        backdropGradient.setBackground(gradientDrawable);
    }

    public void bindViews(MovieEntity entity) {
        setMoviePosterTransition();
        Picasso.with(this)
                .load(Uri.parse(BASE_IMAGE_URL + POSTER_SIZE + entity.getBackdropPath()))
                .into(movieHeader);
        Picasso.with(this)
                .load(Uri.parse(BASE_IMAGE_URL + POSTER_SIZE + entity.getPosterPath()))
                .into(moviePoster);
        movieTitle.setText(entity.getTitle());
        movieReleaseYear.setText(getYearOfRelease(entity.getReleaseDate()));
        movieRating.setText(String.valueOf(entity.getVoteAverage()));
        movieDescription.setText(entity.getOverview());
    }

    private String getYearOfRelease(String releaseDate) {
        String[] parts = releaseDate.split("-");
        return parts[0];
    }

    @Override
    public void onPaletteRetrieved(Palette palette) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(palette.getDominantColor(Color.WHITE));
        String hexColor = String.format("#%06X", (0xFFFFFF & palette.getDominantColor(Color.WHITE)));
        setBackdropGradient(Color.parseColor(hexColor));
        mainBackground.setBackgroundColor(palette.getDominantColor((Color.WHITE)));
        SharedPreferencesUtil.saveInt(SharedPreferencesUtil.MOVIE_DETAILS_MAIN_COLOR, palette.getDominantColor((Color.WHITE)), this);
    }

    @Override
    public void onPaletteFailure() {
        Log.e(TAG, "Palette failure!");
    }
}

