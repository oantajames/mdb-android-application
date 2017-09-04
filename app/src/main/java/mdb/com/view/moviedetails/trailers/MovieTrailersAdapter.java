package mdb.com.view.moviedetails.trailers;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
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
import mdb.com.data.api.entity.MovieVideoEntity;

import java.util.List;

/**
 * @author james on 8/21/17.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {

    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    @Nullable
    private List<MovieVideoEntity> movieVideos;
    @Nullable
    private OnItemClickListener onItemClickListener;

    private Context context;

    public MovieTrailersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_trailer, parent, false);
        return new TrailerViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        if (movieVideos != null) {
            MovieVideoEntity video = movieVideos.get(position);
            Glide.with(context)
                    .load(String.format(YOUTUBE_THUMBNAIL, video.getKey()))
                    .placeholder(Color.GRAY)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(holder.movieVideoThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (movieVideos != null) {
            return movieVideos.size();
        } else {
            return 0;
        }
    }

    public void setMovieVideos(@Nullable List<MovieVideoEntity> movieVideos) {
        this.movieVideos = movieVideos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MovieVideoEntity getItem(int position) {
        if (movieVideos == null || position < 0 || position > movieVideos.size()) {
            return null;
        }
        return movieVideos.get(position);
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.movie_video_thumbnail)
        ImageView movieVideoThumbnail;

        @Nullable
        private OnItemClickListener onItemClickListener;

        public TrailerViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }

    }
}
