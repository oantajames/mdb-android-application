package com.newm.view.mostpopular;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import com.newm.R;
import com.newm.data.api.MovieEntity;
import com.newm.di.component.DaggerMoviesGridComponent;
import com.newm.di.module.MoviesGridModule;
import com.newm.presenter.MoviesGridPresenter;
import com.newm.presenter.MoviesGridPresenterImpl;
import com.newm.view.BaseActivity;
import java.util.List;
import javax.inject.Inject;

public class MoviesGridActivity extends BaseActivity implements MoviesGridPresenterImpl.Delegate {

    private static final int MOST_POPULAR = 0;
    private static final int TOP_RATED = 1;

    public static final int MOVIE_LOADER_ID = 10;

    // TODO: 7/31/17 - CREATE THE MOVIE DETAILS SCREEN

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
        DaggerMoviesGridComponent moviesGridComponent = (DaggerMoviesGridComponent) DaggerMoviesGridComponent.builder()
                .applicationComponent(getApplicationComponent())
                .moviesGridModule(new MoviesGridModule())
                .build();
        moviesGridComponent.inject(this);
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        initMovieGrid();
    }

    private void initMovieGrid() {
        adapter = new MoviesGridAdapter();
        presenter.getPopularMovies(getLoaderManager(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
}
