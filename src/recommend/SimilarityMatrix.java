package recommend;

import recommend.RatingMatrix;

import java.util.*;
import java.util.Set;

/**
 * Created by Luis on 2017/11/14.
 */
public class SimilarityMatrix {
    private int num_user;
    private double[] aUserAverageRating;
    private double[][] similarity;
    private int[][] ratings;
    private static RatingMatrix ratingMatrixForSim;
    //private static List<IdAndRating> intersection;
    private double[][] estimatedRatings;
    private double MSE;


    private static final int TITLES = 3952;
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2TRAIN = "User_train_ratings.dat";
    private static final String FILE_PATH2TEST = "User_train_ratings.dat";
    private static final boolean movie2UserFlag = false;



    public SimilarityMatrix(int num_user) {
        this.num_user = num_user;
        //ratings = new int[num_user][TITLES];
        aUserAverageRating = new double[num_user];
        similarity = new double[num_user][num_user];
        estimatedRatings = new double[num_user][TITLES];


        ratingMatrixForSim = new RatingMatrix();
        ratingMatrixForSim.loadData(decideFilePathTrain(num_user));

        Map<Integer,Set<Integer>> map = ratingMatrixForSim.getUserId2Movie();
        calcAUserAverageRating(num_user);

        //calcMatrix();
        //assert map.get(1).size() == 51;
        MSE = 0.0;
    }


    public static String decideFilePathTrain(int userNum){
        //assert ratingMatrixForSim.getUserId2Movie().get(0).size() == 51;
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2TRAIN;
    }

    public static String decideFilePathTest(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2TEST;
    }

    public double getSimilarity(int user1, int user2) {
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        return similarity[user1-1][user2-1];
    }

    public void setSimilarity(int user1, int user2, double similarityValue) {
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        similarity[user1-1][user2-1] = similarityValue;
        similarity[user2-1][user1-1] = similarityValue;
    }


    public void calcAUserAverageRating(int num_user){
//        assert ratingMatrixForSim.getUserId2Movie().get(0).size() == 51;
        int sum_rating,num_ratedItem;

        for (int userIndex = 1; userIndex <= num_user; userIndex++) {
            sum_rating = 0;
            num_ratedItem = 0;
            for (int titleIndex = 1; titleIndex <= TITLES; titleIndex++) {
                int tmpRating;
                if (ratingMatrixForSim.checkIdExists(userIndex,titleIndex,movie2UserFlag)) {
                    tmpRating = ratingMatrixForSim.getRating(userIndex,titleIndex,movie2UserFlag);
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

    public List<Integer> makeIntersection(int user1, int user2, List<Integer> intersection, boolean movie2UserFlag, Map<Integer,Set<Integer>> userId2Movie){
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        return ratingMatrixForSim.makeListFrom2Ids(user1,user2,intersection,movie2UserFlag, userId2Movie);
    }

    public void calcAndSetSimilarity(int user1, int user2) {
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        double numerator = 0.0;
        double denominator = 0.0;
        double denominator1 = 0.0;
        double denominator2 = 0.0;
        List<Integer> intersection = new ArrayList<>();

        intersection = makeIntersection(user1,user2,intersection,movie2UserFlag, ratingMatrixForSim.getUserId2Movie());
        for(int element : intersection){
            double calc1 = (ratingMatrixForSim.getRating(user1,element,movie2UserFlag) - aUserAverageRating[user1-1]);
            double calc2 = (ratingMatrixForSim.getRating(user2,element,movie2UserFlag) - aUserAverageRating[user2-1]);
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

    public void estimateRating(int userId,int movieId){
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        double atUserAverageRating = aUserAverageRating[userId - 1];
        List<Integer> watchedUserList = new ArrayList<>();
        boolean makeUserList = true;

        watchedUserList = ratingMatrixForSim.makeListFromId(movieId,watchedUserList,makeUserList);

        //その映画を誰も見ていない場合
        //データ中に、アイテムmovieIdを評価しているユーザはユーザuserIdしかいないとき、平均を推定値とする
        if (watchedUserList.isEmpty() ||( watchedUserList.size() < 2 && watchedUserList.get(0) == userId)) {
            setEstimatedRating(userId,movieId,atUserAverageRating);
            return;
        }

        //今見ているuserIdの人と、他の人との類似度の合計が0の時、ゼロ割を防ぐため平均を推定値とする
        //double absSumOtherUserSimilarity =0.0;
        double absSumOtherUserSimilarity = calcAbsSumOtherUserSimilarity(userId,watchedUserList);
        if (isDenominatorZero(absSumOtherUserSimilarity)) {
            setEstimatedRating(userId,movieId,atUserAverageRating);
            return;
        }


        //式(2.3)の計算
        double numerator = 0.0;

        for (int otherUserElement : watchedUserList){
            numerator += getSimilarity(userId,otherUserElement)
                         * (ratingMatrixForSim.getRating(otherUserElement,movieId,movie2UserFlag) - aUserAverageRating[otherUserElement - 1]);
        }

        setEstimatedRating(userId,movieId,atUserAverageRating + (numerator / absSumOtherUserSimilarity));
    }




    public double calcAbsSumOtherUserSimilarity(int userId, List<Integer> watchedUserList){
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        double result = 0.0;
        for (int element : watchedUserList){
            result += Math.abs(getSimilarity(userId,element));
        }
        return result;
    }


    public boolean isDenominatorZero(double absSumOtherUserSimilarity){
        if (absSumOtherUserSimilarity == 0) return true;
        else return false;
    }

    public void setEstimatedRating(int userId, int movieId, double estimatedRating){
        estimatedRatings[userId - 1][movieId - 1] = estimatedRating;
    }

    public double getEstimatedRating(int userId, int movieId){
        return estimatedRatings[userId - 1][movieId - 1];
    }

    public void calcMSE(RatingMatrix testRatingMatrix){
        MSE = 0;
        if (testRatingMatrix == null) System.exit(-1);

        int num_rating = 0;
        List<Integer> watchedMovieList = new ArrayList<>();

        Map<Integer,Set<Integer>> userId2Movie = testRatingMatrix.getUserId2Movie();

        for (Map.Entry<Integer,Set<Integer>> entry : userId2Movie.entrySet()){
            int userId = entry.getKey();
            //if (userId > 10) break;
            for (int movieId : entry.getValue()){
                double trueValue = testRatingMatrix.getRating(userId,movieId,movie2UserFlag);
                double estimatedValue = getEstimatedRating(userId,movieId);


                MSE += (trueValue - estimatedValue) * (trueValue - estimatedValue);
                num_rating++;
            }
        }
        setMSE(Math.sqrt(MSE / num_rating));

    }

    public void setMSE(double result){
        MSE = result;
    }

    public double getMSE(){
        return MSE;
    }

}
