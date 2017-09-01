package mdb.com.view.moviedetails;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import mdb.com.R;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieVideoEntity;
import mdb.com.data.api.entity.ReviewEntity;
import mdb.com.di.component.DaggerMovieDetailsComponent;
import mdb.com.di.module.MovieDetailsModule;
import mdb.com.presenter.moviedetails.MovieDetailsPresenter;
import mdb.com.presenter.moviedetails.MovieDetailsPresenterImpl;
import mdb.com.util.SharedPreferencesUtil;
import mdb.com.view.BaseActivity;
import mdb.com.view.moviedetails.reviews.MovieReviewsAdapter;
import mdb.com.view.moviedetails.trailers.MovieTrailersAdapter;
import mdb.com.view.moviesgrid.MoviesGridActivity;

import java.util.List;

import javax.inject.Inject;

import uk.co.deanwild.flowtextview.FlowTextView;

import static mdb.com.data.api.ApiConstants.BASE_BACKDROP_URL;
import static mdb.com.data.api.ApiConstants.BASE_IMAGE_URL;

/**
 * @author james on 8/1/17.
 */

public class MovieDetailsActivity extends BaseActivity implements MovieDetailsPresenterImpl.PresenterPaletteListener, MovieDetailsPresenterImpl.Delegate {

    public static final String EXTRA_MOVIE_IMAGE_TRANSITION = "movieImageTransition";

    public static final int MOVIE_TRAILERS_LOADER_ID = 11;

    public static final int MOVIE_REVIEWS_LOADER_ID = 12;

    @Inject
    MovieDetailsPresenter presenter;

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
    FlowTextView movieDescription;
    @Bind(R.id.main_background)
    LinearLayout mainBackground;
    @Bind(R.id.trailer_recycler_view)
    RecyclerView trailersRecyclerView;
    @Bind(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;

    private MovieTrailersAdapter movieTrailersAdapter;
    private MovieReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setFlowTextViewAppearance();
        MovieEntity movieEntity = getIntent().getExtras().getParcelable(MoviesGridActivity.MOVIE_ENTITY);
        movieTrailersAdapter = new MovieTrailersAdapter(this);
        reviewsAdapter = new MovieReviewsAdapter();
        if (movieEntity != null) {
            presenter.onCreate(Uri.parse(BASE_IMAGE_URL + movieEntity.getBackdropPath()), this, this);
            presenter.getMovieTrailers(getLoaderManager(), String.valueOf(movieEntity.getId()));
            presenter.getMovieReviews(getLoaderManager(), String.valueOf(movieEntity.getId()));
        }
        bindViews(movieEntity);
    }

    private void setFlowTextViewAppearance() {
        movieDescription.setTextColor(Color.WHITE);
        movieDescription.setTextSize(45);
    }

    private void inject() {
        DaggerMovieDetailsComponent movieDetailsComponent = (DaggerMovieDetailsComponent) DaggerMovieDetailsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .movieDetailsModule(new MovieDetailsModule())
                .build();
        movieDetailsComponent.inject(this);
    }

    public void bindViews(MovieEntity entity) {
        movieTitle.setText(entity.getTitle());
        movieReleaseYear.setText(getYearOfRelease(entity.getReleaseDate()));
        movieRating.setText(String.valueOf(entity.getVoteAverage()));
        movieDescription.setText(entity.getOverview());
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        setGradientViews(Color.BLACK);
        moviePoster.setBackgroundColor(Color.BLACK);
        mainBackground.setBackgroundColor(Color.BLACK);
        SharedPreferencesUtil.saveInt(SharedPreferencesUtil.MOVIE_DETAILS_MAIN_COLOR, (Color.BLACK), this);
        setMoviePosterTransition();
        Glide.with(this)
                .load(Uri.parse(BASE_BACKDROP_URL + entity.getBackdropPath()))
                .placeholder(Color.GRAY)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .crossFade()
                .into(movieHeader);

        Glide.with(this)
                .load(Uri.parse(BASE_IMAGE_URL + entity.getPosterPath()))
                .placeholder(Color.GRAY)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .crossFade()
                .into(moviePoster);


    }

    private void setMoviePosterTransition() {
        Bundle extras = getIntent().getExtras();
        final String transition;
        if (extras != null) {
            transition = extras.getString(EXTRA_MOVIE_IMAGE_TRANSITION);
            moviePoster.setTransitionName(transition);
        }
    }

    private String getYearOfRelease(String releaseDate) {
        String[] parts = releaseDate.split("-");
        return parts[0];
    }

    @Override
    public void onPaletteRetrieved(Palette palette) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        String hexColor = String.format("#%06X", (0xFFFFFF & palette.getDarkVibrantColor(Color.BLACK)));
        setGradientViews(Color.BLACK);
        moviePoster.setBackgroundColor(Color.BLACK);
        mainBackground.setBackgroundColor(Color.BLACK);
        SharedPreferencesUtil.saveInt(SharedPreferencesUtil.MOVIE_DETAILS_MAIN_COLOR, (Color.BLACK), this);
    }

    private void setGradientViews(int colorCode) {
        GradientDrawable bottomToTopGradient = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{colorCode, Color.TRANSPARENT});
        backdropGradient.setBackground(bottomToTopGradient);
    }

    @Override
    public void onPaletteFailure() {
        Log.e(TAG, "Palette failure!");
    }

    @Override
    public void setMovieTrailers(List<MovieVideoEntity> result) {
        movieTrailersAdapter.setOnItemClickListener((itemView, position) -> onMovieVideoClicked(position));
        trailersRecyclerView.setAdapter(movieTrailersAdapter);
        trailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(layoutManager);
        movieTrailersAdapter.setMovieVideos(result);
    }

    @Override
    public void setMovieReviews(List<ReviewEntity> reviews) {
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsAdapter.setReviews(reviews);
    }

    private void onMovieVideoClicked(int position) {
        MovieVideoEntity video = movieTrailersAdapter.getItem(position);
        if (video != null && video.isYoutubeVideo()) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            startActivity(intent);
        }
    }
}

