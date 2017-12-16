import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Luis on 2017/11/15.
 */
public class Test2 {
    private static final int TITLES = 3952;
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2TRAIN = "User_train_ratings.dat";
    private static final String FILE_PATH2TEST = "User_test_ratings.dat";
    private static final String fileName = "100User_similarities.dat";
    private static final String SPLITTER = "::";
    private static BufferedReader in = null;
    private static final int NUM_USER = 10;
    private static final int MAX_USER = 1600;
    private static final boolean movie2UserFlag = false;

    private static long sumCalcTimeSim = 0;



    public static void main(String[] args){
        String readString;
        String[] tmpData;
        int atUser, atMovie;

        try{
            for (int user_num = 100;user_num <= MAX_USER;user_num *= 2){
                double MSE = 0.0;
                int countMSE = 0;
                sumCalcTimeSim =0;
                long sumCalcTimeEstimate = 0;
                int countLoop = 0;

                System.out.println("〜user数が" + user_num + "の時〜\n\n・類似度行列");
                SimilarityMatrix similarityMatrix = new SimilarityMatrix(user_num);
                RatingMatrix testRatingMatrix = new RatingMatrix();
                testRatingMatrix.loadData(decideFilePathTest(user_num));
                //if (user_num == 100) testRatingMatrix.printRatingMatrix();

                //課題2-1 & レポート2-2-1
                printSimilarityMatrix(similarityMatrix,user_num);


                //testRatingMatrix.printRatingMatrix();
                in = new BufferedReader(new FileReader(decideFilePathTest(user_num))); //読み込むファイルをパスで指定
                //↓printする時
                System.out.println("\n・(userId, movieId) -> 推定評価値");

                while((readString = in.readLine()) != null) {
                    tmpData = readString.split(SPLITTER);
                    atUser = Integer.parseInt(tmpData[0]);
                    atMovie = Integer.parseInt(tmpData[1]);

                    //if (atUser > 10) break; ←これのせいでMSEが無茶苦茶になり、私は無限に時間を溶かしました。。。

                    long start = System.nanoTime();
                    similarityMatrix.estimateRating(atUser,atMovie);
                    long end = System.nanoTime();
                    sumCalcTimeEstimate += end - start;

                    double estimatedRating = similarityMatrix.getEstimatedRating(atUser,atMovie);
                    double realRating = testRatingMatrix.getRating(atUser,atMovie,movie2UserFlag);

                    MSE += (realRating - estimatedRating) * (realRating - estimatedRating);
                    countMSE++;

                    //↓printする時
                    if (atUser <= 10){
//                        if (countLoop != 0 && countLoop %2 == 0) {
//                            System.out.println();
//                        }
                        String tmp = String.format("%.2f",estimatedRating);
                        //System.out.println("(" + atUser + "," + atMovie + ") -> (" + tmp + "," + realRating + ")  ");
                        System.out.println("(" + atUser + "," + atMovie + ") -> " + tmp);
                    }
                    countLoop++;
                }
                System.out.println("\n");
                //System.out.println("count : " + countMSE);
                System.out.println("類似度行列の計算時間 : " + sumCalcTimeSim + "(nanosec)");
                System.out.println("推定評価値の計算時間 : " + sumCalcTimeEstimate + "(nanosec)");
                System.out.println("MSE               : " + Math.sqrt(MSE/countMSE) + "\n\n");
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(in != null){
                try{
                    in.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printSimilarityMatrix(SimilarityMatrix similarityMatrix,int num_user){
        int user1,user2;
        System.out.println("       1     2     3     4     5     6     7     8     9    10");
        for (user1 = 1; user1 <= num_user; user1++){
            if (user1 <= 10){
                if (user1 != 10){
                    System.out.print(user1 + " ");
                }
                else {
                    System.out.print(user1);
                }
            }

            for (user2 = 1; user2 <= num_user; user2++){
                //類似度行列計算時間の計測
                long start = System.nanoTime();
                similarityMatrix.calcAndSetSimilarity(user1,user2);
                long end = System.nanoTime();
                sumCalcTimeSim += end - start;

                double tmp = round(similarityMatrix.getSimilarity(user1,user2));
                if (user1 <= 10 && user2 <= 10){
                    if (tmp < 0) {
                        System.out.print(" " + String.format("%.2f",tmp));
                    }
                    else {
                        System.out.print("  " + String.format("%.2f",tmp));
                    }
                }
            }
            if (user1 <= 10) System.out.println();
        }
    }

    public static void matrix2File(int num_user, SimilarityMatrix similarityMatrix) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

            for (int user1 = 1; user1 <= num_user ;user1++){
                //user1との類似度が各行にprintされる
                for (int user2 = 1; user2 <= num_user; user2++){
                    double value = similarityMatrix.getSimilarity(user1,user2);
                    pw.print(String.valueOf(value) + SPLITTER);
                }
                pw.println();
            }
            pw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static double round(double num){
        BigDecimal numDecBefore = new BigDecimal(num);
        BigDecimal numDecAfter = numDecBefore.setScale(2, BigDecimal.ROUND_HALF_UP);
        return numDecAfter.doubleValue();
    }

    public static void file2Matrix(String fileName, SimilarityMatrix similarityMatrix2){
        try {
            String[] strings;
            String readString;
            in = new BufferedReader(new FileReader(fileName)); //読み込むファイルをパスで指定

            while((readString = in.readLine()) != null) {
                strings = readString.split(SPLITTER);
                int user1 = Integer.parseInt(strings[0]);
                int user2 = Integer.parseInt(strings[1]);
                double similarity = Double.parseDouble(strings[2]);

                similarityMatrix2.setSimilarity(user1,user2,similarity);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String decideFilePathTest(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2TEST;
    }

}
