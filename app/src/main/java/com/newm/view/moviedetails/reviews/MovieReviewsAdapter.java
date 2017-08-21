package com.newm.view.moviedetails.reviews;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.newm.R;
import com.newm.data.api.entity.ReviewEntity;
import java.util.List;

/**
 * @author james on 8/21/17.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {

    @Nullable
    private List<ReviewEntity> reviews;


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (reviews != null) {
            holder.reviewTitle.setText(reviews.get(position).getAuthor());
            holder.description.setText(reviews.get(position).getContent());
        }

    }

    @Override
    public int getItemCount() {
        if (reviews != null) {
            return reviews.size();
        } else {
            return 0;
        }
    }

    public void setReviews(List<ReviewEntity> list) {
        this.reviews = list;
        notifyDataSetChanged();
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
}
