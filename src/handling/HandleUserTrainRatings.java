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
    private static final int movie1 = 1;
    private static final int movie2 = 5;
    private static final int user1 = 1;
    private static final int user2 = 5;
    private static final boolean printTitleTable = true;
    private static final int userNum = 1600;

    public static void main(String[] args){
        List<IdAndRating> user1List = new ArrayList<>();
        List<IdAndRating> user2List = new ArrayList<>();
        RatingMatrix0 ratingMatrix0 = new RatingMatrix0();
        ratingMatrix0.loadData(decideFilePath(userNum));
        //ratingMatrix0.printUserRatingList(movie1);
        //ratingMatrix.printMovieRatingList(user1);
        //ratingMatrix0.printMovieRatingListFrom2Users(user1,user2,printTitleTable);
        ratingMatrix0.printUserRatingListFrom2Movies(movie1,movie2,printTitleTable);

    }

    public static String decideFilePath(int userNum){
        return filePath1 + String.valueOf(userNum) + filePath2;
    }


}
