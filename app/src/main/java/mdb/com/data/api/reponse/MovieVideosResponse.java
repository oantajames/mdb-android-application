package mdb.com.data.api.reponse;

import com.google.gson.annotations.SerializedName;
import mdb.com.data.api.entity.MovieVideoEntity;
import java.util.ArrayList;
import java.util.List;

public class MovieVideosResponse {
    @SerializedName("id")
    private long movieId;

    @SerializedName("results")
    private List<MovieVideoEntity> trailers;

    public MovieVideosResponse(long movieId, ArrayList<MovieVideoEntity> trailers) {
        this.movieId = movieId;
        this.trailers = trailers;
    }

    public long getMovieId() {
        return movieId;
    }

    public List<MovieVideoEntity> getTrailers() {
        return trailers;
    }
}
