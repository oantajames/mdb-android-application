package com.newm.view.mostpopular;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.newm.R;
import com.newm.data.api.MovieEntity;
import com.newm.loaders.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author james on 7/23/17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private List<MovieEntity> movieEntities = new ArrayList<>();
    private MovieClickListener listener;


    public interface MovieClickListener {
        void movieClicked(long movieId, int position);
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
        this.movieEntities.clear();
        this.movieEntities = movieList;
        this.notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageLoader imageLoader = new ImageLoader();

        @Bind(R.id.grid_item)
        SimpleDraweeView simpleDraweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMovieItem(final MovieEntity movieItem, final int position) {
            imageLoader.loadImageToView(movieItem.getPosterPath(), simpleDraweeView);
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) return;
                    listener.movieClicked(movieItem.getId(), position);
                }
            });
        }

    }
}
