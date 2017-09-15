package mdb.com.data.api;

import io.reactivex.Observable;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.reponse.QueryMoviesResponse;
import mdb.com.data.api.reponse.MovieReviewsResponse;
import mdb.com.data.api.reponse.MovieVideosResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author james on 7/17/17.
 */

public interface MoviesService {

    @GET("movie/{id}/reviews")
    Observable<MovieReviewsResponse> getMovieReviews(@Path("id") String movieId);

    @GET("movie/{id}/videos")
    Observable<MovieVideosResponse> getMovieVideos(@Path("id") String movieId);

    @GET("discover/movie")
    Observable<QueryMoviesResponse<MovieEntity>> queryMovies(@Query("sort_by") String sortBy, @Query("page") Integer page);
}
