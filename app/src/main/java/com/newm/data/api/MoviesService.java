package com.newm.data.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author james on 7/17/17.
 */

public interface MoviesService {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesList();

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRateMoviesList();

}
