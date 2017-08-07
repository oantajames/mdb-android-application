package com.newm.view.moviesgrid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.newm.R;
import com.newm.data.api.MovieEntity;
import com.newm.di.component.DaggerMoviesGridComponent;
import com.newm.di.module.MoviesGridModule;
import com.newm.presenter.moviesgrid.MoviesGridPresenter;
import com.newm.presenter.moviesgrid.MoviesGridPresenterImpl;
import com.newm.util.SharedPreferencesUtil;
import com.newm.view.BaseActivity;
import com.newm.view.moviedetails.MovieDetailsActivity;
import java.util.List;
import javax.inject.Inject;

public class MoviesGridActivity extends BaseActivity implements MoviesGridPresenterImpl.Delegate, MoviesGridAdapter.MovieClickListener {

    private static final int MOST_POPULAR = 0;
    private static final int TOP_RATED = 1;

    public static final int MOVIE_LOADER_ID = 10;
    public static String MOVIE_ENTITY = "movie_entity";

    @Bind(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    @Inject
    MoviesGridPresenter presenter;

    private int currentSortType;

    private MoviesGridAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DaggerMoviesGridComponent moviesGridComponent = (DaggerMoviesGridComponent) DaggerMoviesGridComponent.builder()
                .applicationComponent(getApplicationComponent())
                .moviesGridModule(new MoviesGridModule())
                .build();
        moviesGridComponent.inject(this);
        initMovieGrid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveMovieDetailsMainColor();
    }

    private void initMovieGrid() {
        adapter = new MoviesGridAdapter(this);
        presenter.getPopularMovies(getLoaderManager(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void retrieveMovieDetailsMainColor() {
        int detailsBackground = SharedPreferencesUtil.getInt(SharedPreferencesUtil.MOVIE_DETAILS_MAIN_COLOR, 0, this);
        if (detailsBackground != 0) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(detailsBackground);
            recyclerView.setBackgroundColor(detailsBackground);

            Drawable currentBG = recyclerView.getBackground();
            ColorDrawable newBG = new ColorDrawable(detailsBackground);
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{currentBG, newBG});
            transitionDrawable.setCrossFadeEnabled(true);
            recyclerView.setBackgroundDrawable(transitionDrawable);
            transitionDrawable.startTransition(1000);
        }
    }

    @Override
    public void setMoviesList(List<MovieEntity> result) {
        adapter.setMovieList(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSortMostPopular:
                setSortType(MOST_POPULAR);
                return true;
            case R.id.actionSortTopRated:
                setSortType(TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSortType(int sortType) {
        switch (sortType) {
            case MOST_POPULAR:
                presenter.getPopularMovies(getLoaderManager(), this);
                getSupportActionBar().setTitle(getString(R.string.action_most_popular));
                break;
            case TOP_RATED:
                presenter.getTopRatedMovies(getLoaderManager(), this);
                getSupportActionBar().setTitle(getString(R.string.action_top_rated));
                break;
        }
    }

    @Override
    public void movieClicked(MovieEntity movieEntity, ImageView moviePoster) {

        ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(this, moviePoster, ViewCompat.getTransitionName(moviePoster));
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_IMAGE_TRANSITION, ViewCompat.getTransitionName(moviePoster));
        intent.putExtra(MOVIE_ENTITY, movieEntity);
        startActivity(intent, optionsCompat.toBundle());
    }
}
