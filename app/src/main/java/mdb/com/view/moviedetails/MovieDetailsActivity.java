package mdb.com.view.moviedetails;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import mdb.com.R;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieTrailerEntity;
import mdb.com.data.db.MoviesContract;
import mdb.com.di.component.DaggerMovieDetailsComponent;
import mdb.com.di.module.MovieDetailsModule;
import mdb.com.repository.FavoritesRepository;
import mdb.com.repository.MovieDetailsRepository;
import mdb.com.repository.MoviesRepository;
import mdb.com.util.rx.DisposableManager;
import mdb.com.util.SharedPreferencesUtil;
import mdb.com.view.base.BaseActivity;
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

public class MovieDetailsActivity extends BaseActivity implements MovieReviewsAdapter.UpdateReviewsView, MovieTrailersAdapter.UpdateTrailersView {

    public static final String EXTRA_MOVIE_IMAGE_TRANSITION = "movieImageTransition";

    private static final int TRAILERS_LOADER = 1;
    private static final int REVIEWS_LOADER = 2;

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
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.no_reviews)
    TextView noReviewsView;
    @Bind(R.id.no_trailers)
    TextView noTrailersView;


    @Inject
    FavoritesRepository favoritesRepository;
    @Inject
    MovieDetailsRepository movieDetailsRepository;

    private MovieTrailersAdapter movieTrailersAdapter;
    private MovieReviewsAdapter reviewsAdapter;
    private MovieEntity movieEntity;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MovieDetailsRepository.BROADCAST_ON_COMPLETE_TRAILERS)) {
                if (!intent.getBooleanExtra(MovieDetailsRepository.SUCCESSFUL_UPDATED, true)) {
                    showError();
                }
            } else if (action.equals(MovieDetailsRepository.BROADCAST_ON_COMPLETE_REVIEWS)) {
                if (!intent.getBooleanExtra(MovieDetailsRepository.SUCCESSFUL_UPDATED, true)) {
                    showError();
                }
            }
        }
    };

    private void showError() {
        Snackbar.make(scrollView, R.string.error_failed_to_update_movies,
                Snackbar.LENGTH_LONG)
                .show();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setFlowTextViewAppearance();
        movieEntity = getIntent().getExtras().getParcelable(MoviesGridActivity.MOVIE_ENTITY);
        getLoaderManager().initLoader(TRAILERS_LOADER, null, trailersCallback);
        getLoaderManager().initLoader(REVIEWS_LOADER, null, reviewsCallback);
        setFavoritesButtonView();
        movieTrailersAdapter = new MovieTrailersAdapter(null, this);
        reviewsAdapter = new MovieReviewsAdapter(null, this);
        bindViews(movieEntity);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieDetailsRepository.BROADCAST_ON_COMPLETE_REVIEWS);
        intentFilter.addAction(MovieDetailsRepository.BROADCAST_ON_COMPLETE_TRAILERS);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        movieDetailsRepository.retrieveTrailers(movieEntity);
        movieDetailsRepository.retrieveReviews(movieEntity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }


    private void setFavoritesButtonView() {
        if (favoritesRepository.isFavorite(movieEntity)) {
            favoritesButton.setSelected(true);
        } else {
            favoritesButton.setSelected(false);
        }
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
        MovieTrailerEntity video = movieTrailersAdapter.getItem(position);
        if (video != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            startActivity(intent);
        }
    }

    private final LoaderManager.LoaderCallbacks<Cursor> trailersCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(), ContentUris.withAppendedId(MoviesContract.TrailersEntry.CONTENT_URI, movieEntity.getId())
                    , null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            movieTrailersAdapter.changeCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            movieTrailersAdapter.changeCursor(null);
        }
    };


    private final LoaderManager.LoaderCallbacks<Cursor> reviewsCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(), ContentUris.withAppendedId(MoviesContract.ReviewsEntry.CONTENT_URI, movieEntity.getId())
                    , null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            reviewsAdapter.changeCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            reviewsAdapter.changeCursor(null);
        }
    };

    @Override
    public void updateReviewsViewForEmptyData() {
        noReviewsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateTrailersViewForEmptyData() {
        noTrailersView.setVisibility(View.VISIBLE);
    }
}

