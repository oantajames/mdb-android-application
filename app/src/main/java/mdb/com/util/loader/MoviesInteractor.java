package mdb.com.util.loader;

import android.util.Log;
import mdb.com.data.api.entity.MovieEntity;
import mdb.com.data.api.entity.MovieVideoEntity;
import mdb.com.data.api.entity.ReviewEntity;
import mdb.com.data.api.reponse.MovieResponse;
import mdb.com.data.api.MoviesService;
import mdb.com.data.api.reponse.MovieReviewsResponse;
import mdb.com.data.api.reponse.MovieVideosResponse;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static mdb.com.sync.Sort.MOST_POPULAR;
import static mdb.com.sync.Sort.TOP_RATED;

/**
 * @author james on 7/27/17.
 */
@Singleton
public class MoviesInteractor {


    @Inject
    public MoviesInteractor() {
    }

//    private List<MovieEntity> createPopularMoviesCall(MoviesService service) {
//        try {
//            Response<MovieResponse> response = service.getPopularMoviesList().execute();
//            if (response.isSuccessful()) {
//                return response.body().getResults();
//            } else {
//                Log.e(TAG, "unsuccessfully loaded...");
//                Log.e(TAG, String.valueOf(response.errorBody()));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private List<MovieEntity> createTopRatedMoviesCall(MoviesService service) {
        try {
            Response<MovieResponse> response = service.getTopRateMoviesList().execute();
            if (response.isSuccessful()) {
                return response.body().getResults();
            } else {
                Log.e(TAG, "unsuccessfully loaded...");
                Log.e(TAG, String.valueOf(response.errorBody()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<MovieVideoEntity> createMovieTrailersCall(MoviesService service, String movieId) {
        try {
            Response<MovieVideosResponse> response = service.getMovieVideos(movieId).execute();
            if (response.isSuccessful()) {
                return response.body().getTrailers();
            } else {
                Log.e(TAG, "unsuccessfully loaded...");
                Log.e(TAG, String.valueOf(response.errorBody()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ReviewEntity> createMovieReviewsCall(MoviesService service, String movieId) {
        try {
            Response<MovieReviewsResponse> response = service.getMovieReviews(movieId).execute();
            if (response.isSuccessful()) {
                return response.body().getReviews();
            } else {
                Log.e(TAG, "unsuccessfully loaded...");
                Log.e(TAG, String.valueOf(response.errorBody()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
