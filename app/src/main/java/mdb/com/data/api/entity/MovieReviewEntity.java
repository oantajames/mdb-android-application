package mdb.com.data.api.entity;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static mdb.com.data.db.MoviesContract.ReviewsEntry;
import static mdb.com.data.db.MoviesContract.getColumnInt;
import static mdb.com.data.db.MoviesContract.getColumnString;


@SuppressWarnings("ALL")
public class MovieReviewEntity {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("url")
    @Expose
    public String url;
    public int movieId;

    public MovieReviewEntity() {

    }

    public MovieReviewEntity(Cursor cursor) {
        this.id = getColumnString(cursor, ReviewsEntry._ID);
        this.author = getColumnString(cursor, ReviewsEntry.AUTHOR);
        this.content = getColumnString(cursor, ReviewsEntry.CONTENT);
        this.url = getColumnString(cursor, ReviewsEntry.URL);
        this.movieId = getColumnInt(cursor, ReviewsEntry.MOVIE_ID);
    }

    public static ContentValues convert(MovieReviewEntity movieReviewEntity, int movieId) {
        ContentValues content = new ContentValues();
        content.put(ReviewsEntry._ID, movieReviewEntity.id);
        content.put(ReviewsEntry.AUTHOR, movieReviewEntity.author);
        content.put(ReviewsEntry.CONTENT, movieReviewEntity.content);
        content.put(ReviewsEntry.MOVIE_ID, movieId);
        content.put(ReviewsEntry.URL, movieReviewEntity.url);
        return content;
    }

    public static MovieReviewEntity fromCursor(Cursor cursor) {
        MovieReviewEntity movieReviewEntity = new MovieReviewEntity();
        movieReviewEntity.setMovieId(getColumnInt(cursor, ReviewsEntry.MOVIE_ID));
        movieReviewEntity.setId(getColumnString(cursor, ReviewsEntry._ID));
        movieReviewEntity.setAuthor(getColumnString(cursor, ReviewsEntry.AUTHOR));
        movieReviewEntity.setContent(getColumnString(cursor, ReviewsEntry.CONTENT));
        movieReviewEntity.setUrl(getColumnString(cursor, ReviewsEntry.URL));
        return movieReviewEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
