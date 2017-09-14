package mdb.com.data.api.reponse;

import com.google.gson.annotations.SerializedName;
import mdb.com.data.api.entity.MovieTrailerEntity;
import java.util.ArrayList;
import java.util.List;

public class MovieVideosResponse {
    @SerializedName("id")
    private long movieId;

    @SerializedName("results")
    private List<MovieTrailerEntity> trailers;

    public MovieVideosResponse(long movieId, ArrayList<MovieTrailerEntity> trailers) {
        this.movieId = movieId;
        this.trailers = trailers;
    }

    public long getMovieId() {
        return movieId;
    }

    public List<MovieTrailerEntity> getTrailers() {
        return trailers;
    }
}
