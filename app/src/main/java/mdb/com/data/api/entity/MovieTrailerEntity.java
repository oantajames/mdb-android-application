package mdb.com.data.api.entity;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static mdb.com.data.db.MoviesContract.TrailersEntry;
import static mdb.com.data.db.MoviesContract.getColumnInt;
import static mdb.com.data.db.MoviesContract.getColumnString;

@SuppressWarnings("ALL")
public class MovieTrailerEntity {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    public String iso31661;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("site")
    @Expose
    public String site;
    @SerializedName("size")
    @Expose
    public Integer size;
    @SerializedName("type")
    @Expose
    public String type;
    public int movieId;

    public MovieTrailerEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public MovieTrailerEntity(Cursor cursor) {
        this.id = getColumnString(cursor, TrailersEntry._ID);
        this.iso6391 = getColumnString(cursor, TrailersEntry.ISO6391);
        this.iso31661 = getColumnString(cursor, TrailersEntry.ISO31661);
        this.key = getColumnString(cursor, TrailersEntry.KEY);
        this.name = getColumnString(cursor, TrailersEntry.NAME);
        this.site = getColumnString(cursor, TrailersEntry.SITE);
        this.size = getColumnInt(cursor, TrailersEntry.SIZE);
        this.type = getColumnString(cursor, TrailersEntry.TYPE);
        this.movieId = getColumnInt(cursor, TrailersEntry.MOVIE_ID);
    }

    public static ContentValues convert(MovieTrailerEntity movieTrailerEntity, int movieId) {
        ContentValues content = new ContentValues();
        content.put(TrailersEntry._ID, movieTrailerEntity.id);
        content.put(TrailersEntry.ISO6391, movieTrailerEntity.iso6391);
        content.put(TrailersEntry.ISO31661, movieTrailerEntity.iso31661);
        content.put(TrailersEntry.KEY, movieTrailerEntity.key);
        content.put(TrailersEntry.NAME, movieTrailerEntity.name);
        content.put(TrailersEntry.SITE, movieTrailerEntity.site);
        content.put(TrailersEntry.SIZE, movieTrailerEntity.size);
        content.put(TrailersEntry.TYPE, movieTrailerEntity.type);
        content.put(TrailersEntry.MOVIE_ID, movieId);
        return content;
    }

    public static MovieTrailerEntity fromCursor(Cursor cursor) {
        String id = String.valueOf(Math.toIntExact(cursor.getLong(cursor.getColumnIndex(TrailersEntry._ID))));
        String name = cursor.getString(cursor.getColumnIndex(TrailersEntry.NAME));
        MovieTrailerEntity movieTrailerEntity = new MovieTrailerEntity(id, name);
        movieTrailerEntity.setIso6391(getColumnString(cursor, TrailersEntry.ISO6391));
        movieTrailerEntity.setIso31661(getColumnString(cursor, TrailersEntry.ISO31661));
        movieTrailerEntity.setKey(getColumnString(cursor, TrailersEntry.KEY));
        movieTrailerEntity.setType(getColumnString(cursor, TrailersEntry.TYPE));
        movieTrailerEntity.setSite(getColumnString(cursor, TrailersEntry.SITE));
        movieTrailerEntity.setSize(getColumnInt(cursor, TrailersEntry.SIZE));
        movieTrailerEntity.setId(getColumnString(cursor, TrailersEntry._ID));
        movieTrailerEntity.setName(getColumnString(cursor, TrailersEntry.NAME));
        movieTrailerEntity.setMovieId(getColumnInt(cursor, TrailersEntry.MOVIE_ID));
        return movieTrailerEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
