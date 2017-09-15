package mdb.com.data.api.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import mdb.com.data.db.MoviesContract;

import static mdb.com.data.db.MoviesContract.MovieEntry;
import static mdb.com.data.db.MoviesContract.getColumnDouble;
import static mdb.com.data.db.MoviesContract.getColumnInt;
import static mdb.com.data.db.MoviesContract.getColumnString;

public class MovieEntity implements Parcelable {

    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("video")
    @Expose
    public Boolean video;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;

    public MovieEntity(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public MovieEntity(Cursor cursor) {
        this.id = getColumnInt(cursor, MovieEntry._ID);
        this.title = getColumnString(cursor, MovieEntry.TITLE);
        this.posterPath = getColumnString(cursor, MovieEntry.POSTER_PATH);
        this.adult = getColumnInt(cursor, MovieEntry.IS_ADULT) == 1;
        this.backdropPath = getColumnString(cursor, MovieEntry.BACKDROP_PATH);
        this.originalLanguage = getColumnString(cursor, MovieEntry.ORIGINAL_LANGUAGE);
        this.overview = getColumnString(cursor, MovieEntry.OVERVIEW);
        this.popularity = getColumnDouble(cursor, MovieEntry.POPULARITY);
        this.originalTitle = getColumnString(cursor, MovieEntry.ORIGINAL_TITLE);
        this.releaseDate = getColumnString(cursor, MovieEntry.RELEASE_DATE);
        this.video = getColumnInt(cursor, MovieEntry.HAS_VIDEO) == 1;
        this.voteAverage = getColumnDouble(cursor, MovieEntry.VOTE_AVERAGE);
        this.voteCount = getColumnInt(cursor, MovieEntry.VOTE_COUNT);
    }

    protected MovieEntity(Parcel in) {
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        byte tmpVideo = in.readByte();
        video = tmpVideo == 0 ? null : tmpVideo == 1;
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
        title = in.readString();
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readDouble();
        }
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
        overview = in.readString();
        releaseDate = in.readString();
        byte tmpIsFavorite = in.readByte();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (voteCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(voteCount);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeByte((byte) (video == null ? 0 : video ? 1 : 2));
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
        dest.writeString(title);
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(popularity);
        }
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieEntity> CREATOR = new Creator<MovieEntity>() {
        @Override
        public MovieEntity createFromParcel(Parcel in) {
            return new MovieEntity(in);
        }

        @Override
        public MovieEntity[] newArray(int size) {
            return new MovieEntity[size];
        }
    };

    public ContentValues convertToContentValues() {
        ContentValues movieContent = new ContentValues();
        movieContent.put(MovieEntry._ID, id);
        movieContent.put(MovieEntry.TITLE, title);
        movieContent.put(MovieEntry.POSTER_PATH, posterPath);
        movieContent.put(MovieEntry.IS_ADULT, adult);
        movieContent.put(MovieEntry.BACKDROP_PATH, backdropPath);
        movieContent.put(MovieEntry.ORIGINAL_LANGUAGE, originalLanguage);
        movieContent.put(MovieEntry.OVERVIEW, overview);
        movieContent.put(MovieEntry.POPULARITY, popularity);
        movieContent.put(MovieEntry.ORIGINAL_TITLE, originalTitle);
        movieContent.put(MovieEntry.RELEASE_DATE, releaseDate);
        movieContent.put(MovieEntry.HAS_VIDEO, video);
        movieContent.put(MovieEntry.VOTE_AVERAGE, voteAverage);
        movieContent.put(MovieEntry.VOTE_COUNT, voteCount);
        return movieContent;
    }

    public static MovieEntity fromCursor(Cursor cursor) {
        int id = Math.toIntExact(cursor.getLong(cursor.getColumnIndex(MovieEntry._ID)));
        String title = cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE));
        MovieEntity movie = new MovieEntity(id, title);
        movie.setId(getColumnInt(cursor, MovieEntry._ID));
        movie.setTitle(getColumnString(cursor, MovieEntry.TITLE));
        movie.setPosterPath(getColumnString(cursor, MovieEntry.POSTER_PATH));
        movie.setAdult(getColumnInt(cursor, MovieEntry.IS_ADULT) == 1);
        movie.setBackdropPath(getColumnString(cursor, MovieEntry.BACKDROP_PATH));
        movie.setOriginalLanguage(getColumnString(cursor, MovieEntry.ORIGINAL_LANGUAGE));
        movie.setOverview(getColumnString(cursor, MovieEntry.OVERVIEW));
        movie.setPopularity(getColumnDouble(cursor, MovieEntry.POPULARITY));
        movie.setOriginalTitle(getColumnString(cursor, MovieEntry.ORIGINAL_TITLE));
        movie.setReleaseDate(getColumnString(cursor, MovieEntry.RELEASE_DATE));
        movie.setVideo(getColumnInt(cursor, MovieEntry.HAS_VIDEO) == 1);
        movie.setVoteAverage(getColumnDouble(cursor, MovieEntry.VOTE_AVERAGE));
        movie.setVoteCount(getColumnInt(cursor, MovieEntry.VOTE_COUNT));
        return movie;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static Creator<MovieEntity> getCREATOR() {
        return CREATOR;
    }

}
