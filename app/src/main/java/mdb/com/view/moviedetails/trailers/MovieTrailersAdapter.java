package mdb.com.view.moviedetails.trailers;

import android.content.Context;
import android.database.Cursor;
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
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieTrailerEntity;

import java.util.List;
import mdb.com.view.moviesgrid.util.CursorRecyclerViewAdapter;

/**
 * @author james on 8/21/17.
 */

public class MovieTrailersAdapter extends CursorRecyclerViewAdapter<MovieTrailersAdapter.TrailerViewHolder> {

    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    @Nullable
    private OnItemClickListener onItemClickListener;

    private Context context;

    public MovieTrailersAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_trailer, parent, false);
        return new TrailerViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder viewHolder, Cursor cursor) {
        if (cursor != null) {
            MovieTrailerEntity trailerEntity = MovieTrailerEntity.fromCursor(cursor);
            viewHolder.setVideoEntity(trailerEntity);
        }
    }

    @Nullable
    public MovieTrailerEntity getItem(int position) {
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
        return MovieTrailerEntity.fromCursor(cursor);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.movie_video_thumbnail)
        ImageView movieVideoThumbnail;
        private MovieTrailerEntity entity;

        @Nullable
        private OnItemClickListener onItemClickListener;

        public TrailerViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }

        void setVideoEntity(MovieTrailerEntity trailerEntity) {
            this.entity = trailerEntity;
            Glide.with(context)
                    .load(String.format(YOUTUBE_THUMBNAIL, trailerEntity.getKey()))
                    .placeholder(Color.GRAY)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(movieVideoThumbnail);
        }

    }
}
