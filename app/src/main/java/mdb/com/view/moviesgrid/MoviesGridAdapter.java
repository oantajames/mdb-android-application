package mdb.com.view.moviesgrid;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import javax.inject.Inject;
import mdb.com.R;
import mdb.com.data.api.ApiConstants;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.di.component.MoviesGridComponent;
import mdb.com.sync.FavoriteService;
import mdb.com.view.moviesgrid.util.CursorRecyclerViewAdapter;
import mdb.com.view.moviesgrid.util.OnItemClickListener;

/**
 * @author james on 7/23/17.
 */

public class MoviesGridAdapter extends CursorRecyclerViewAdapter<MoviesGridAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public MoviesGridAdapter(Cursor cursor) {
        super(cursor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        if (cursor != null) {
            MovieEntity movieEntity = MovieEntity.fromCursor(cursor);
            viewHolder.setMovieItem(movieEntity);
        }
    }

    @Nullable
    public MovieEntity getItem(int position) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return null;
        }
        if (position < 0 || position > cursor.getCount()) {
            return null;
        }
        cursor.moveToFirst();
        for (int i = 0; i < position; i++) {
            cursor.moveToNext();
        }
        return MovieEntity.fromCursor(cursor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.movie_poster)
        ImageView moviePoster;
        @Bind(R.id.grid_favorite_button)
        ImageView favoriteButton;

        @Inject
        FavoriteService favoriteService;

        private final Context context;
        private OnItemClickListener onItemClickListener;
        private MovieEntity movieEntity;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            ((MoviesGridActivity) context).getComponent().inject(this);
            this.onItemClickListener = listener;
        }

        public void setMovieItem(final MovieEntity movieEntity) {
            this.movieEntity = movieEntity;
            setupFavoritesButton();
            ViewCompat.setTransitionName(moviePoster, movieEntity.getTitle());
            Glide.with(context)
                    .load(Uri.parse(ApiConstants.BASE_IMAGE_URL + movieEntity.getPosterPath()))
                    .placeholder(Color.GRAY)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(moviePoster);
            itemView.setOnClickListener(this);
        }

        private void setupFavoritesButton() {
            if (favoriteService.isFavorite(movieEntity)) {
                favoriteButton.setSelected(true);
            } else {
                favoriteButton.setSelected(false);
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @OnClick(R.id.grid_favorite_button)
        public void onFavoritesButton(View view) {
            if (view.isSelected()) {
                favoriteService.removeFromFavorites(movieEntity);
                view.setSelected(false);
            } else {
                favoriteService.addToFavorites(movieEntity);
                view.setSelected(true);
            }
        }
    }
}
