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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import mdb.com.R;
import mdb.com.data.api.ApiConstants;
import mdb.com.data.api.entity.MovieEntity;
import java.util.ArrayList;
import java.util.List;
import mdb.com.view.moviesgrid.util.CursorRecyclerViewAdapter;
import mdb.com.view.moviesgrid.util.OnItemClickListener;

/**
 * @author james on 7/23/17.
 */

public class MoviesGridAdapter extends CursorRecyclerViewAdapter<MoviesGridAdapter.ViewHolder> {

    private List<MovieEntity> movieEntities = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;

    public MoviesGridAdapter(Context context, Cursor cursor) {
        super(cursor);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface MovieClickListener {
        void movieClicked(MovieEntity movieEntity, ImageView moviePoster);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (movieEntities == null) return 0;
        return movieEntities.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        if (cursor != null) {
            MovieEntity movieEntity = MovieEntity.fromCursor(cursor);
            viewHolder.setMovieItem(movieEntity);
        }
    }

    public void setMovieList(List<MovieEntity> movieList) {
        if (movieList != null) {
            this.movieEntities.clear();
            this.movieEntities = movieList;
            this.notifyDataSetChanged();
        }
    }

    public MovieEntity getMovieEntity(int position) {
        if (movieEntities.get(position) == null) {
            throw new IllegalArgumentException("Invalid position");
        }
        return movieEntities.get(position);
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
        private final Context context;

        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void setMovieItem(final MovieEntity movieItem) {
            ViewCompat.setTransitionName(moviePoster, movieItem.getTitle());
            Glide.with(context)
                    .load(Uri.parse(ApiConstants.BASE_IMAGE_URL + movieItem.getPosterPath()))
                    .placeholder(Color.GRAY)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(moviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
