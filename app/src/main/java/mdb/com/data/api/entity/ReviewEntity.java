package mdb.com.data.api.entity;

/**
 * @author james on 8/20/17.
 */

public class ReviewEntity {

    private String id;

    private String author;

    private String content;

    private String url;

    private int movieId;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public int getMovieId() {
        return movieId;
    }

}
