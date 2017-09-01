package com.newm.view.moviesgrid;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.newm.R;
import com.newm.data.api.ApiConstants;
import com.newm.data.api.entity.MovieEntity;
import com.newm.loaders.ImageLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author james on 7/23/17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private List<MovieEntity> movieEntities = new ArrayList<>();
    private MovieClickListener listener;

    public MoviesGridAdapter(MovieClickListener listener) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setMovieItem(movieEntities.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (movieEntities == null) return 0;
        return movieEntities.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageLoader imageLoader = new ImageLoader();

        @Bind(R.id.movie_poster)
        ImageView moviePoster;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void setMovieItem(final MovieEntity movieItem, final int position) {
            ViewCompat.setTransitionName(moviePoster, getMovieEntity(position).getTitle());
            Glide.with(context)
                    .load(Uri.parse(ApiConstants.BASE_IMAGE_URL + movieItem.getPosterPath()))
                    .placeholder(Color.GRAY)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(moviePoster);

            moviePoster.setOnClickListener(v -> {
                if (listener == null) return;
                listener.movieClicked(movieItem, moviePoster);
            });
        }
    }
}
