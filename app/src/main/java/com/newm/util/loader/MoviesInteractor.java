package com.newm.util.loader;

import android.util.Log;
import com.newm.data.api.entity.MovieEntity;
import com.newm.data.api.entity.MovieVideoEntity;
import com.newm.data.api.reponse.MovieResponse;
import com.newm.data.api.MoviesService;
import com.newm.data.api.reponse.MovieVideosResponse;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * @author james on 7/27/17.
 */
@Singleton
public class MoviesInteractor {


    @Inject
    public MoviesInteractor() {
    }

    public List<MovieEntity> createPopularMoviesCall(MoviesService service) {
        try {
            Response<MovieResponse> response = service.getPopularMoviesList().execute();
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

    public List<MovieEntity> createTopRatedMoviesCall(MoviesService service) {
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


}
