package mdb.com.data.api.reponse;

import com.google.gson.annotations.SerializedName;
import mdb.com.data.api.entity.ReviewEntity;
import java.util.List;

/**
 * @author james on 8/20/17.
 */

public class MovieReviewsResponse {

    public int movieId;

    public int page;
    @SerializedName("results")
    public List<ReviewEntity> reviews;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_results")
    public int totalResults;

    public int getMovieId() {
        return movieId;
    }

    public int getPage() {
        return page;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
