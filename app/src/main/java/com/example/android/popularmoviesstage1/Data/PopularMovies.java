package com.example.android.popularmoviesstage1.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KUNAL on 24-02-2016.
 */
public class PopularMovies implements Parcelable{

    Integer movieId;
    String imageUrl;
    String title;
    String thumbNail;
    String overView;
    String average;
    String releaseDate;

    public Integer getMovieId() {
        return movieId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public String getOverView() {
        return overView;
    }

    public String getAverage() {
        return average;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public static Creator<PopularMovies> getCREATOR() {
        return CREATOR;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public PopularMovies(int movieId,String imageUrl,String title,
                         String thumbNail,String overView, String average, String releaseDate) {
        this.movieId = movieId;
        this.imageUrl =imageUrl;
        this.title = title;
        this.thumbNail = thumbNail;
        this.overView = overView;
        this.average = average;
        this.releaseDate = releaseDate;
    }


    protected PopularMovies(Parcel in) {
        movieId = in.readInt();
        imageUrl = in.readString();
        title = in.readString();
        thumbNail = in.readString();
        overView = in.readString();
        average = in.readString();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<PopularMovies> CREATOR = new Parcelable.Creator<PopularMovies>() {
        @Override
        public PopularMovies createFromParcel(Parcel in) {
            return new PopularMovies(in);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(thumbNail);
        dest.writeString(overView);
        dest.writeString(average);
        dest.writeString(releaseDate);
    }
}
