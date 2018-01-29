import com.sun.xml.internal.ws.api.ha.StickyFeature;

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

    private static final String FILE_PATH_QUESTIONNAIRE_TRAIN = "res/questionnaire/";
    private static final String FILE_PATH_QUESTIONNAIRE_TEST = "res/questionnaire/";


    private boolean isItemSim;


    public SimilarityMatrix(int param1, int param2, boolean isItemSim) {
        //param1がMovielensのやつでいうuser,param2がmovieに該当する
        this.num_user = param1;
        this.isItemSim = isItemSim;

        ratingMatrixForSim = new RatingMatrix();

        if (isItemSim) {
            aUserAverageRating = new double[param2];
            similarity = new double[param2][param2];
            estimatedRatings = new double[param2][param1];
            //MovieLensの時
            ratingMatrixForSim.loadData(decideFilePathTrain(param1),isItemSim);
            //アンケートの時
            //ratingMatrixForSim.loadData(FILE_PATH_QUESTIONNAIRE_TRAIN);
            calcAUserAverageRating(param2,param1);

            long sumCalcTimeSim = 0;

            for (int user1 = 1;user1 <= param2;user1++){
                for (int user2 = 1;user2 <= param2;user2++){
                    long start = System.nanoTime();
                    calcAndSetSimilarity(user1,user2);
                    long end = System.nanoTime();
                    sumCalcTimeSim += end - start;
                }
            }
            System.out.println("類似度行列の計算時間 : " + sumCalcTimeSim + "(nanosec)\n");

        } else {
            aUserAverageRating = new double[param1];
            similarity = new double[param1][param1];
            estimatedRatings = new double[param1][param2];
            ratingMatrixForSim.loadData(decideFilePathTrain(param1));
            calcAUserAverageRating(param1,param2);
        }

        //Map<Integer,Set<Integer>> map = ratingMatrixForSim.getUserId2Movie();


        //calcMatrix();
        //assert map.get(1).size() == 51;
        this.MSE = 0.0;
    }

    public SimilarityMatrix(int num_user,String filePathCSV, int num_restaurant){
        this.num_user = num_user;
        aUserAverageRating = new double[num_user];
        similarity = new double[num_user][num_user];
        estimatedRatings = new double[num_user][num_restaurant];

        ratingMatrixForSim = new RatingMatrix();
        ratingMatrixForSim.loadCSV(filePathCSV);

        calcAUserAverageRating(num_user,num_restaurant);

        for (int user1 = 1;user1 <= num_user;user1++){
            for (int user2 = 1;user2 <= num_user;user2++){
                calcAndSetSimilarity(user1,user2);
            }
        }
        this.MSE = 0.0;
    }


    public int getRealRating(int user, int item){
        if (ratingMatrixForSim.checkIdExists(user,item,false)){
            return ratingMatrixForSim.getRating(user,item,false);
        } else {
            return 0;
        }
    }

    public double getAUserAverageRating(int num_user){
        return aUserAverageRating[num_user];
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

    public void calcAUserAverageRating(int param1,int param2){
//        assert ratingMatrixForSim.getUserId2Movie().get(0).size() == 51;
        int sum_rating,num_ratedItem;

        for (int userIndex = 1; userIndex <= param1; userIndex++) {
            sum_rating = 0;
            num_ratedItem = 0;
            for (int restaurantIndex = 1; restaurantIndex <= param2; restaurantIndex++) {
                //if (userIndex == 2239) System.out.println(restaurantIndex);
                int tmpRating;
                if (ratingMatrixForSim.checkIdExists(userIndex,restaurantIndex,movie2UserFlag)) {
                    tmpRating = ratingMatrixForSim.getRating(userIndex,restaurantIndex,movie2UserFlag);
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

    public void calcAndSetItemSimilarity(int item1, int item2) {
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        double numerator = 0.0;
        double denominator;
        double denominator1 = 0.0;
        double denominator2 = 0.0;
        List<Integer> intersection = new ArrayList<>();

        intersection = makeIntersection(item1,item2,intersection,movie2UserFlag, ratingMatrixForSim.getUserId2Movie());
        for(int element : intersection){
            double calc1 = (ratingMatrixForSim.getRating(item1,element,movie2UserFlag) - aUserAverageRating[item1-1]);
            double calc2 = (ratingMatrixForSim.getRating(item2,element,movie2UserFlag) - aUserAverageRating[item2-1]);
            //分子の計算
            numerator += calc1 * calc2;
            //分母の計算
            denominator1 += calc1 * calc1;
            denominator2 += calc2 * calc2;
        }
        denominator = Math.sqrt(denominator1) * Math.sqrt(denominator2);

        if (intersection.size() <= 1 || denominator1 == 0 || denominator2 == 0) setSimilarity(item1,item2,0);
        else setSimilarity(item1,item2,numerator/denominator);

    }

    public void estimateRating(int userId,int movieId){
        //assert ratingMatrixForSim.getUserId2Movie().get(1).size() == 51;
        double atUserAverageRating = aUserAverageRating[userId - 1];
        List<Integer> watchedUserList = new ArrayList<>();
        boolean makeUserList = true;
        //if (isItemSim) makeUserList = false;

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
        System.out.print("");
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
        //System.out.println(userId + "," + movieId);
        estimatedRatings[userId - 1][movieId - 1] = estimatedRating;
    }

    public double getEstimatedRating(int userId, int movieId){
        return estimatedRatings[userId - 1][movieId - 1];
    }

    public void calcMSE(){
        MSE = 0;
        if (ratingMatrixForSim == null) System.exit(-1);

        int num_rating = 0;
        List<Integer> watchedMovieList = new ArrayList<>();

        Map<Integer,Set<Integer>> userId2Movie = ratingMatrixForSim.getUserId2Movie();

        for (Map.Entry<Integer,Set<Integer>> entry : userId2Movie.entrySet()){
            int userId = entry.getKey();
            //if (userId > 10) break;
            for (int movieId : entry.getValue()){
                double trueValue = ratingMatrixForSim.getRating(userId,movieId,movie2UserFlag);
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
