package com.newm.view.moviesgrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.newm.NewmApplication;
import com.newm.R;
import com.newm.data.api.entity.MovieEntity;
import com.newm.di.component.DaggerMoviesGridComponent;
import com.newm.di.module.MoviesGridModule;
import com.newm.presenter.moviesgrid.MoviesGridPresenter;
import com.newm.presenter.moviesgrid.MoviesGridPresenterImpl;
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
    @Bind(R.id.movie_grid_progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.main_mogie_grid_background)
    ViewGroup mainBackground;

    @Inject
    MoviesGridPresenter presenter;

    private int currentSortType;

    private MoviesGridAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        inject();
        getSupportActionBar().setTitle(getString(R.string.action_most_popular));
        initMovieGrid();
    }

    private void inject() {
        DaggerMoviesGridComponent moviesGridComponent = (DaggerMoviesGridComponent) DaggerMoviesGridComponent.builder()
                .applicationComponent(getApplicationComponent())
                .moviesGridModule(new MoviesGridModule())
                .build();
        moviesGridComponent.inject(this);
    }

    private void initMovieGrid() {
        adapter = new MoviesGridAdapter(this);
        presenter.getPopularMovies(getLoaderManager(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setMoviesList(List<MovieEntity> result) {
        adapter.setMovieList(result);
        progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.VISIBLE);
                setSortType(MOST_POPULAR);
                return true;
            case R.id.actionSortTopRated:
                progressBar.setVisibility(View.VISIBLE);
                setSortType(TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSortType(int sortType) {
        switch (sortType) {
            case MOST_POPULAR:
                if (((NewmApplication) getApplication()).isNetworkConnection()) {
                    presenter.getPopularMovies(getLoaderManager(), this);
                    getSupportActionBar().setTitle(getString(R.string.action_most_popular));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(mainBackground, "No internet connection!", Snackbar.LENGTH_LONG).show();

                }

                break;
            case TOP_RATED:
                if (((NewmApplication) getApplication()).isNetworkConnection()) {
                    presenter.getTopRatedMovies(getLoaderManager(), this);
                    getSupportActionBar().setTitle(getString(R.string.action_top_rated));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(mainBackground, "No internet connection!", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void movieClicked(MovieEntity movieEntity, ImageView moviePoster) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ENTITY, movieEntity);
        startActivity(intent);
    }
}
