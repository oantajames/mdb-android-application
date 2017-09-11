package mdb.com.view.moviedetails;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import mdb.com.R;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieVideoEntity;
import mdb.com.di.component.DaggerMovieDetailsComponent;
import mdb.com.di.module.MovieDetailsModule;
import mdb.com.sync.FavoritesRepository;
import mdb.com.util.SharedPreferencesUtil;
import mdb.com.view.BaseActivity;
import mdb.com.view.moviedetails.reviews.MovieReviewsAdapter;
import mdb.com.view.moviedetails.trailers.MovieTrailersAdapter;
import mdb.com.view.moviesgrid.MoviesGridActivity;

import javax.inject.Inject;

import uk.co.deanwild.flowtextview.FlowTextView;

import static mdb.com.data.api.ApiConstants.BASE_BACKDROP_URL;
import static mdb.com.data.api.ApiConstants.BASE_IMAGE_URL;

/**
 * @author james on 8/1/17.
 */

public class MovieDetailsActivity extends BaseActivity {

    public static final String EXTRA_MOVIE_IMAGE_TRANSITION = "movieImageTransition";

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
    @Bind(R.id.favorites_button)
    ImageView favoritesButton;
    @Bind(R.id.back_button)
    ImageView backButton;

    @Inject
    FavoritesRepository favoritesRepository;
    @Inject
    MovieDetailsRepository movieDetailsRepository;

    private MovieTrailersAdapter movieTrailersAdapter;
    private MovieReviewsAdapter reviewsAdapter;
    private MovieEntity movieEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setFlowTextViewAppearance();
        movieEntity = getIntent().getExtras().getParcelable(MoviesGridActivity.MOVIE_ENTITY);
        setFavoritesButtonView();
        movieTrailersAdapter = new MovieTrailersAdapter(this);
        reviewsAdapter = new MovieReviewsAdapter();
        bindViews(movieEntity);
    }

    private void setFavoritesButtonView() {
        if (favoritesRepository.isFavorite(movieEntity)) {
            favoritesButton.setSelected(true);
        } else {
            favoritesButton.setSelected(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        movieDetailsRepository.loadMovieVideos(movieEntity);
        movieDetailsRepository.loadMovieReviews(movieEntity);
    }

    private void setFlowTextViewAppearance() {
        movieDescription.setTextColor(Color.WHITE);
        movieDescription.setTextSize(60);
    }

    private void inject() {
        DaggerMovieDetailsComponent movieDetailsComponent = (DaggerMovieDetailsComponent) DaggerMovieDetailsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .movieDetailsModule(new MovieDetailsModule())
                .build();
        movieDetailsComponent.inject(this);
    }

    @OnClick(R.id.favorites_button)
    public void onFavoritesClick(View v) {
        if (v.isSelected()) {
            favoritesRepository.removeFromFavorites(movieEntity);
            v.setSelected(false);
        } else {
            favoritesRepository.addToFavorites(movieEntity);
            v.setSelected(true);
        }
    }

    @OnClick(R.id.back_button)
    public void onBackButtonClicked() {
        super.onBackPressed();
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(movieHeader);

        Glide.with(this)
                .load(Uri.parse(BASE_IMAGE_URL + entity.getPosterPath()))
                .placeholder(Color.GRAY)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(moviePoster);
        initTrailersView();
        initReviewsView();
    }

    private void initTrailersView() {
        movieTrailersAdapter.setOnItemClickListener((itemView, position) -> onMovieVideoClicked(position));
        trailersRecyclerView.setAdapter(movieTrailersAdapter);
        trailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(layoutManager);
    }

    private void initReviewsView() {
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);
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

    private void setGradientViews(int colorCode) {
        GradientDrawable bottomToTopGradient = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{colorCode, Color.TRANSPARENT});
        backdropGradient.setBackground(bottomToTopGradient);
    }

    private void onMovieVideoClicked(int position) {
        MovieVideoEntity video = movieTrailersAdapter.getItem(position);
        if (video != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            startActivity(intent);
        }
    }


}

