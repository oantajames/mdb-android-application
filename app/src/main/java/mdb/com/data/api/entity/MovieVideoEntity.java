package mdb.com.data.api.entity;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static mdb.com.data.db.MoviesContract.TrailerColumns;
import static mdb.com.data.db.MoviesContract.getColumnInt;
import static mdb.com.data.db.MoviesContract.getColumnString;

@SuppressWarnings("ALL")
public class MovieVideoEntity {

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

    public MovieVideoEntity(Cursor cursor) {
        this.id = getColumnString(cursor, TrailerColumns._ID);
        this.iso6391 = getColumnString(cursor, TrailerColumns.ISO6391);
        this.iso31661 = getColumnString(cursor, TrailerColumns.ISO31661);
        this.key = getColumnString(cursor, TrailerColumns.KEY);
        this.name = getColumnString(cursor, TrailerColumns.NAME);
        this.site = getColumnString(cursor, TrailerColumns.SITE);
        this.size = getColumnInt(cursor, TrailerColumns.SIZE);
        this.type = getColumnString(cursor, TrailerColumns.TYPE);
        this.movieId = getColumnInt(cursor, TrailerColumns.MOVIE_ID);
    }

    public static ContentValues convert(MovieVideoEntity movieVideoEntity, int movieId) {
        ContentValues content = new ContentValues();
        content.put(TrailerColumns._ID, movieVideoEntity.id);
        content.put(TrailerColumns.ISO6391, movieVideoEntity.iso6391);
        content.put(TrailerColumns.ISO31661, movieVideoEntity.iso31661);
        content.put(TrailerColumns.KEY, movieVideoEntity.key);
        content.put(TrailerColumns.NAME, movieVideoEntity.name);
        content.put(TrailerColumns.SITE, movieVideoEntity.site);
        content.put(TrailerColumns.SIZE, movieVideoEntity.size);
        content.put(TrailerColumns.TYPE, movieVideoEntity.type);
        content.put(TrailerColumns.MOVIE_ID, movieId);
        return content;
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
