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
import android.util.Log;
import mdb.com.R;
import mdb.com.di.component.MoviesGridComponent;
import mdb.com.repository.MoviesRepository;
import mdb.com.util.Sort;

import javax.inject.Inject;

import mdb.com.util.Utils;
import mdb.com.view.moviesgrid.util.AbstractMoviesGridFragment;
import mdb.com.view.moviesgrid.util.EndlessRecyclerViewOnScrollListener;

/**
 * @author james on 8/31/17.
 */

public class FragmentMoviesList extends AbstractMoviesGridFragment {

    public static final String SORT = "SORT";

    @Inject
    MoviesRepository moviesRepository;
    private String sort;

    private EndlessRecyclerViewOnScrollListener endlessRecyclerViewOnScrollListener;

    public static FragmentMoviesList newInstance(String state) {
        FragmentMoviesList fragmentMoviesList = new FragmentMoviesList();
        Bundle bundle = new Bundle();
        bundle.putString(SORT, state);
        fragmentMoviesList.setArguments(bundle);
        return fragmentMoviesList;
    }

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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
        if (endlessRecyclerViewOnScrollListener != null) {
            Log.d(SORT, moviesRepository.toString());
            Log.d(SORT, sort);
            endlessRecyclerViewOnScrollListener.setLoading(moviesRepository.isLoading());
        }
        swipeRefreshLayout.setRefreshing(moviesRepository.isLoading());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @NonNull
    @Override
    protected Uri getContentUri() {
        return Utils.getSortedMoviesUri(sort);
    }

    @Override
    protected void onCursorLoaded(Cursor data) {
        // the first time the favorites cursor data is empty
        if (sort.equals(Sort.FAVORITES)) {
            getAdapter().changeCursor(data);
        } else {
            getAdapter().changeCursor(data);
            if (data == null || data.getCount() == 0) {
                refreshMovies();
            }
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
