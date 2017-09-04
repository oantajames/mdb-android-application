package mdb.com.view.moviesgrid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import mdb.com.sync.Sort;
import mdb.com.sync.SortHelper;
import mdb.com.sync.MoviesService;
import mdb.com.view.moviedetails.MovieDetailsActivity;


import javax.inject.Inject;

import mdb.com.view.moviesgrid.util.AbstractMoviesGridFragment;
import mdb.com.view.moviesgrid.util.EndlessRecyclerViewOnScrollListener;

/**
 * @author james on 8/31/17.
 */

public class FragmentMoviesList extends AbstractMoviesGridFragment implements MoviesGridAdapter.MovieClickListener {

    public static FragmentMoviesList newInstance(String state) {
        FragmentMoviesList fragmentMoviesList = new FragmentMoviesList();
        Bundle bundle = new Bundle();
        bundle.putString(STATE, state);
        fragmentMoviesList.setArguments(bundle);
        return fragmentMoviesList;
    }

    public static final String BROADCAST_SORT_PREFERENCE_CHANGED = "SortPreferenceChanged";

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
    SortHelper sortHelper;
    @Inject
    MoviesService moviesService;

    private EndlessRecyclerViewOnScrollListener endlessRecyclerViewOnScrollListener;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MoviesService.BROADCAST_UPDATE_FINISHED)) {
                if (!intent.getBooleanExtra(MoviesService.EXTRA_IS_SUCCESSFUL_UPDATED, true)) {
                    Snackbar.make(swipeRefreshLayout, R.string.error_failed_to_update_movies,
                            Snackbar.LENGTH_LONG)
                            .show();
                }
                swipeRefreshLayout.setRefreshing(false);
                endlessRecyclerViewOnScrollListener.setLoading(false);
                updateGridLayout();
            } else if (action.equals(BROADCAST_SORT_PREFERENCE_CHANGED)) {
                recyclerView.smoothScrollToPosition(0);
                restartLoader();
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MoviesGridComponent.class).inject(this);
        sortHelper.saveSortByPreference(Sort.fromString(getArguments().getString(STATE, String.valueOf(Sort.MOST_POPULAR))));
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MoviesService.BROADCAST_UPDATE_FINISHED);
        intentFilter.addAction(BROADCAST_SORT_PREFERENCE_CHANGED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
        if (endlessRecyclerViewOnScrollListener != null) {
            endlessRecyclerViewOnScrollListener.setLoading(moviesService.isLoading());
        }
        swipeRefreshLayout.setRefreshing(moviesService.isLoading());
    }

    @NonNull
    @Override
    protected Uri getContentUri() {
        return sortHelper.getSortedMoviesUri();
    }

    @Override
    protected void onCursorLoaded(Cursor data) {
        if (getAdapter()!=null) {
            getAdapter().changeCursor(data);
        }
        if (data == null || data.getCount() == 0) {
            refreshMovies();
        }
    }

    private void refreshMovies() {
        swipeRefreshLayout.setRefreshing(true);
        moviesService.refreshMovies();
    }


    @Override
    protected void onRefreshAction() {
        refreshMovies();
    }

    @Override
    protected void onMoviesGridInitialisationFinished() {
        endlessRecyclerViewOnScrollListener = new EndlessRecyclerViewOnScrollListener(getGridLayoutManager()) {
            @Override
            public void onLoadMore() {
                swipeRefreshLayout.setRefreshing(true);
                moviesService.loadMoreMovies();
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewOnScrollListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mdb.com.R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void movieClicked(MovieEntity movieEntity, ImageView moviePoster) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MoviesGridActivity.MOVIE_ENTITY, movieEntity);
        startActivity(intent);
    }

}
