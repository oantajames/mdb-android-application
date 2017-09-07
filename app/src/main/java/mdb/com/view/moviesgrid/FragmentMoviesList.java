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

import mdb.com.R;
import mdb.com.di.component.MoviesGridComponent;
import mdb.com.sync.MoviesRepository;
import mdb.com.sync.Sort;
import mdb.com.sync.SortHelper;


import javax.inject.Inject;

import mdb.com.view.moviesgrid.util.AbstractMoviesGridFragment;
import mdb.com.view.moviesgrid.util.EndlessRecyclerViewOnScrollListener;

/**
 * @author james on 8/31/17.
 */

public class FragmentMoviesList extends AbstractMoviesGridFragment {

    public static FragmentMoviesList newInstance(String state) {
        FragmentMoviesList fragmentMoviesList = new FragmentMoviesList();
        Bundle bundle = new Bundle();
        bundle.putString(SORT, state);
        fragmentMoviesList.setArguments(bundle);
        return fragmentMoviesList;
    }

    public static final String BROADCAST_SORT_PREFERENCE_CHANGED = "SortPreferenceChanged";

    public static final String SORT = "SORT";
    private String sort;

    @Inject
    SortHelper sortHelper;
    @Inject
    MoviesRepository moviesRepository;

    private EndlessRecyclerViewOnScrollListener endlessRecyclerViewOnScrollListener;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MoviesRepository.BROADCAST_UPDATE_FINISHED)) {
                if (!intent.getBooleanExtra(MoviesRepository.EXTRA_IS_SUCCESSFUL_UPDATED, true)) {
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
        sort = getArguments().getString(SORT, String.valueOf(Sort.MOST_POPULAR));
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MoviesRepository.BROADCAST_UPDATE_FINISHED);
        intentFilter.addAction(BROADCAST_SORT_PREFERENCE_CHANGED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
        if (endlessRecyclerViewOnScrollListener != null) {
            endlessRecyclerViewOnScrollListener.setLoading(moviesRepository.isLoading());
        }
        swipeRefreshLayout.setRefreshing(moviesRepository.isLoading());
    }

    @NonNull
    @Override
    protected Uri getContentUri() {
        return sortHelper.getSortedMoviesUri(sort);
    }

    @Override
    protected void onCursorLoaded(Cursor data) {
        getAdapter().changeCursor(data);
        if (data == null || data.getCount() == 0) {
            refreshMovies();
        }
    }

    private void refreshMovies() {
        swipeRefreshLayout.setRefreshing(true);
        moviesRepository.refreshMovies(sort);
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
                moviesRepository.loadMoreMovies(sort);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewOnScrollListener);
    }

}
