package com.newm.data.api;

import com.newm.data.api.reponse.MovieResponse;
import com.newm.data.api.reponse.MovieReviewsResponse;
import com.newm.data.api.reponse.MovieVideosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author james on 7/17/17.
 */

public interface MoviesService {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesList();

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRateMoviesList();

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path("id") String movieId);

    @GET("movie/{id}/videos")
    Call<MovieVideosResponse> getMovieVideos(@Path("id") String movieId);

}
