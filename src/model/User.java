package model;

import java.util.List;

/**
 * Created by Luis on 2017/10/12.
 */
public class User {
    private int userId;
    private List<Rating> ratingList;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }



}
