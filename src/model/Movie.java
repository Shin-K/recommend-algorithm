package model;

import java.util.List;

/**
 * Created by Luis on 2017/10/12.
 */
public class Movie {
    private int movieId;
    private String title;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    private List<Rating> ratingList;



}
