package mdb.com.view.moviedetails.reviews;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import mdb.com.R;
import mdb.com.data.api.entity.MovieReviewEntity;
import mdb.com.view.moviesgrid.util.CursorRecyclerViewAdapter;

/**
 * @author james on 8/21/17.
 */

public class MovieReviewsAdapter extends CursorRecyclerViewAdapter<MovieReviewsAdapter.ReviewViewHolder> {

    private UpdateReviewsView updateReviewsView;

    public MovieReviewsAdapter(Cursor cursor, UpdateReviewsView updateReviewsView) {
        super(cursor);
        this.updateReviewsView = updateReviewsView;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder viewHolder, Cursor cursor) {
        MovieReviewEntity reviewEntity = MovieReviewEntity.fromCursor(cursor);
        viewHolder.reviewTitle.setText(reviewEntity.getAuthor());
        viewHolder.description.setText(reviewEntity.getContent());

    }

    @Override
    public void onEmptyCursor() {
        updateReviewsView.updateReviewsViewForEmptyData();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.review_title)
        TextView reviewTitle;
        @Bind(R.id.review_description)
        TextView description;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface UpdateReviewsView {
        void updateReviewsViewForEmptyData();
    }
}
