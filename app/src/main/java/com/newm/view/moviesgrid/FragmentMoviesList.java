package com.newm.view.moviesgrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.newm.R;
import com.newm.data.api.entity.MovieEntity;
import com.newm.di.component.MoviesGridComponent;
import com.newm.presenter.moviesgrid.MoviesGridPresenter;
import com.newm.presenter.moviesgrid.MoviesGridPresenterImpl;
import com.newm.util.BaseFragment;
import com.newm.view.moviedetails.MovieDetailsActivity;
import java.util.List;
import javax.inject.Inject;

import static com.newm.view.moviesgrid.MoviesGridActivity.MOVIE_ENTITY;

/**
 * @author james on 8/31/17.
 */

public class FragmentMoviesList extends BaseFragment implements MoviesGridPresenterImpl.Delegate, MoviesGridAdapter.MovieClickListener {

    public static FragmentMoviesList newInstance(int state) {
        FragmentMoviesList fragmentMoviesList = new FragmentMoviesList();
        Bundle bundle = new Bundle();
        bundle.putInt(STATE, state);
        fragmentMoviesList.setArguments(bundle);
        return fragmentMoviesList;
    }

    public static final int MOST_POPULAR = 11234;
    public static final int TOP_RATED = 21234;
    public static final int MY_FAVORITES = 31223;

    public static final String STATE = "STATE";

    @Bind(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.grid_progress_bar)
    ProgressBar progressBar;

    @Inject
    MoviesGridPresenter presenter;

    private MoviesGridAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MoviesGridComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
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
        intent.putExtra(MOVIE_ENTITY, movieEntity);
        startActivity(intent);
    }

    @Override
    public void setMoviesList(List<MovieEntity> result) {
        adapter.setMovieList(result);
        progressBar.setVisibility(View.GONE);
    }
}
