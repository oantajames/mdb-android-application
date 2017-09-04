package mdb.com.view.moviesgrid;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import mdb.com.R;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.di.component.MoviesGridComponent;
import mdb.com.presenter.moviesgrid.MoviesGridPresenter;
import mdb.com.presenter.moviesgrid.MoviesGridPresenterImpl;
import mdb.com.sync.SortHelper;
import mdb.com.util.BaseFragment;
import mdb.com.view.moviedetails.MovieDetailsActivity;
import java.util.List;
import javax.inject.Inject;
import mdb.com.view.moviesgrid.util.AbstractMoviesGridFragment;

/**
 * @author james on 8/31/17.
 */

public class FragmentMoviesList extends AbstractMoviesGridFragment implements
        MoviesGridPresenterImpl.Delegate, MoviesGridAdapter.MovieClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static FragmentMoviesList newInstance(int state) {
        FragmentMoviesList fragmentMoviesList = new FragmentMoviesList();
        Bundle bundle = new Bundle();
        bundle.putInt(STATE, state);
        fragmentMoviesList.setArguments(bundle);
        return fragmentMoviesList;
    }

    //todo - finish the fragment implementation

    public static final int MOST_POPULAR = 11234;
    public static final int TOP_RATED = 21234;
    public static final int MY_FAVORITES = 31223;

    public static final String STATE = "STATE";

    @Bind(mdb.com.R.id.movies_recycler_view)
    RecyclerView recyclerView;
    @Bind(mdb.com.R.id.grid_progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    MoviesGridPresenter presenter;

    @Inject
    SortHelper sortHelper;

    private MoviesGridAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MoviesGridComponent.class).inject(this);
    }

    @NonNull
    @Override
    protected Uri getContentUri() {
        return sortHelper.getSortedMoviesUri();
    }

    @Override
    protected void onCursorLoaded(Cursor data) {
        getAdapter().changeCursor(data);
        if (data != null || data.getCount() == 0) {
            rerfeshMovies();
        }
    }

    private void refreshMovies() {
        swipeRefreshLayout.setRefreshing(true);
        moviesService.refreshMovies();
    }


    @Override
    protected void onRefreshAction() {

    }

    @Override
    protected void onMoviesGridInitialisationFinished() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mdb.com.R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        initMovieGrid(getArguments().getInt(STATE));
        return view;
    }

    private void initMovieGrid(int state) {
        adapter = new MoviesGridAdapter(this);
        getMovies(state);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getMovies(int state) {
        progressBar.setVisibility(View.VISIBLE);
        switch (state) {
            case MOST_POPULAR:
                presenter.getPopularMovies(getActivity().getLoaderManager(), this, MOST_POPULAR);
                break;
            case TOP_RATED:
                presenter.getTopRatedMovies(getActivity().getLoaderManager(), this, TOP_RATED);
                break;
            case MY_FAVORITES:
                presenter.getPopularMovies(getActivity().getLoaderManager(), this, MY_FAVORITES);
                break;
        }
    }

    @Override
    public void movieClicked(MovieEntity movieEntity, ImageView moviePoster) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MoviesGridActivity.MOVIE_ENTITY, movieEntity);
        startActivity(intent);
    }

    @Override
    public void setMoviesList(List<MovieEntity> result) {
        adapter.setMovieList(result);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
