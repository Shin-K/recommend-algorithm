package handling;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 2017/10/18.
 */
public class HandleUserTrainRatings {
    private static final String filePath1 = "res/exp_data/";
    private static final String filePath2 = "User_train_ratings.dat";
    private static final int TITLES = 3952;
    private static final int movie1 = 5;
    private static final int movie2 = 10;
    private static final int user1 = 1;
    private static final int user2 = 5;
    private static final boolean printTitleTable = true;
    private static final int userNum = 100;

    public static void main(String[] args){
        List<IdAndRating> user1List = new ArrayList<>();
        List<IdAndRating> user2List = new ArrayList<>();
        RatingMatrix ratingMatrix = new RatingMatrix();
        ratingMatrix.loadData(decideFilePath(userNum));
        //ratingMatrix.printUserRatingList(movie1);
        //ratingMatrix.printMovieRatingList(user1);
        //ratingMatrix.printMovieRatingListFrom2Users(user1,user2,printTitleTable);
        ratingMatrix.printUserRatingListFrom2Movies(movie1,movie2,printTitleTable);

    }

    public static String decideFilePath(int userNum){
        return filePath1 + String.valueOf(userNum) + filePath2;
    }


}
