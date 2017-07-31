package com.newm.util.loader;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.newm.data.api.MovieEntity;
import com.newm.data.api.MovieResponse;
import com.newm.data.api.MoviesService;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.*;
import retrofit2.Callback;
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


}
