package mdb.com.data.api;

import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.reponse.DiscoverAndSearchResponse;
import mdb.com.data.api.reponse.MovieResponse;
import mdb.com.data.api.reponse.MovieReviewsResponse;
import mdb.com.data.api.reponse.MovieVideosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author james on 7/17/17.
 */

public interface MoviesService {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMoviesList();

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRateMoviesList();

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path("id") String movieId);

    @GET("movie/{id}/videos")
    Call<MovieVideosResponse> getMovieVideos(@Path("id") String movieId);

    @GET("discover/movie")
    Observable<DiscoverAndSearchResponse<MovieEntity>> discoverMovies(@Query("sort_by") String sortBy,
                                                                      @Query("page") Integer page);
}
