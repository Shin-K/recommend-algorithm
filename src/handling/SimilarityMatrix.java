package handling;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 2017/11/14.
 */
public class SimilarityMatrix {
    private int num_user;
    private double[] aUserAverageRating;
    private double[][] similarity;
    private int[][] ratings;
    private static RatingMatrix ratingMatrix;
    private static List<IdAndRating> intersection;


    private static final int TITLES = 3952;
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2 = "User_train_ratings.dat";
    private static final boolean movie2UserFlag = false;

    private double[][] estimatedRatings = new double[num_user][TITLES];



    public SimilarityMatrix(int num_user) {
        this.num_user = num_user;
        //ratings = new int[num_user][TITLES];
        aUserAverageRating = new double[num_user];
        similarity = new double[num_user][num_user];
        intersection = new ArrayList<>();

        ratingMatrix = new RatingMatrix();
        ratingMatrix.loadData(decideFilePath(num_user));
        calcAUserAverageRating(num_user);
        //calcMatrix();
    }


    public static String decideFilePath(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2;
    }

    public double getSimilarity(int user1, int user2) {
        return similarity[user1-1][user2-1];
    }

    public void setSimilarity(int user1, int user2, double similarityValue) {
        similarity[user1-1][user2-1] = similarityValue;
        similarity[user2-1][user1-1] = similarityValue;
    }


    public void calcAUserAverageRating(int num_user){
        int sum_rating,num_ratedItem;

        for (int userIndex = 1; userIndex <= num_user; userIndex++) {
            sum_rating = 0;
            num_ratedItem = 0;
            for (int titleIndex = 1; titleIndex <= TITLES; titleIndex++) {
                int tmpRating;
                if (ratingMatrix.checkIdExists(userIndex,titleIndex,movie2UserFlag)) {
                    tmpRating = ratingMatrix.getRating(userIndex,titleIndex,movie2UserFlag);
                }
                else tmpRating = 0;

                if (tmpRating != 0) {
                    sum_rating += tmpRating;
                    num_ratedItem++;
                }
            }
            aUserAverageRating[userIndex-1] = (double)sum_rating / num_ratedItem;
        }

    }

    public List<IdAndRating> makeIntersection(int user1, int user2, List<IdAndRating> intersection, boolean movie2UserFlag){
        return ratingMatrix.makeListFrom2Ids(user1,user2,intersection,movie2UserFlag);
    }

    public void calcAndSetSimilarity(int user1, int user2) {
        double numerator = 0.0;
        double denominator = 0.0;
        double denominator1 = 0.0;
        double denominator2 = 0.0;

        intersection = makeIntersection(user1,user2,intersection,movie2UserFlag);
        for(IdAndRating element : intersection){
            double calc1 = (ratingMatrix.getRating(user1,element.getId(),movie2UserFlag) - aUserAverageRating[user1-1]);
            double calc2 = (ratingMatrix.getRating(user2,element.getId(),movie2UserFlag) - aUserAverageRating[user2-1]);
            //分子の計算
            numerator += calc1 * calc2;
            //分母の計算
            denominator1 += calc1 * calc1;
            denominator2 += calc2 * calc2;
        }
        denominator = Math.sqrt(denominator1) * Math.sqrt(denominator2);

        if (intersection.size() <= 1 || denominator1 == 0 || denominator2 == 0) setSimilarity(user1,user2,0);
        else setSimilarity(user1,user2,numerator/denominator);

    }

    //TODO 2.2 not finished
    public double estimateRating(int userId,int movieId){
        double result = 0.0;
        double atUserAverageRating = aUserAverageRating[userId];
        List<IdAndRating> watchedUserList = new ArrayList<>();
        boolean makeUserList = true;

        watchedUserList = ratingMatrix.makeListFromId(movieId,watchedUserList,makeUserList);

        //その映画を誰も見ていない場合
        //if (watchedUserList.isEmpty()) return 0.0;
        //データ中に、アイテムmovieIdを評価しているユーザはユーザuserIdしかいないとき、0を返す
        if (watchedUserList.size() < 2 && watchedUserList.get(0).getId() == userId) return atUserAverageRating;

        return result;
    }




}
