package com.newm.data.api.reponse;

import com.google.gson.annotations.SerializedName;
import com.newm.data.api.entity.ReviewEntity;
import java.util.List;

/**
 * @author james on 8/20/17.
 */

public class MovieReviewsResponse {

    public int movieId;

    public int page;

    public List<ReviewEntity> reviews;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_results")
    public int totalResults;
}
