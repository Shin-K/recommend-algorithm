import java.io.*;
import java.math.BigDecimal;

/**
 * Created by Luis on 2017/11/15.
 */
public class Test2 {
    private static final int TITLES = 3952;
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2TRAIN = "User_train_ratings.dat";
    private static final String FILE_PATH2TEST = "User_test_ratings.dat";
    private static final String fileName = "100User_similarities.dat";
    private static BufferedReader in = null;
    private static final String SPLITTER = "::";
    private static final int NUM_USER = 10;
    private static final int MAX_USER = 1600;



    public static void main(String[] args){
        boolean doTrainNotTest = true;
        //課題2-1
        SimilarityMatrix similarityMatrix = new SimilarityMatrix(NUM_USER,doTrainNotTest);
        report2_2_1(similarityMatrix);

        //matrix2File(NUM_USER,similarityMatrix);
        //SimilarityMatrix similarityMatrix2 = new SimilarityMatrix(NUM_USER,doTrainNotTest); //出力用の行列
        //file2Matrix(fileName,similarityMatrix2);


        //課題2-2
        //similarity2Rating(similarityMatrix);

        //課題2-3
        //rateMSE(doTrainNotTest);
    }

    public static void report2_2_1(SimilarityMatrix similarityMatrix){
        int user1,user2;
        for (user1 = 1; user1 <= NUM_USER; user1++){
            for (user2 = 1; user2 <= NUM_USER; user2++){
                similarityMatrix.calcAndSetSimilarity(user1,user2);
                double tmp = round(similarityMatrix.getSimilarity(user1,user2));
                System.out.print(tmp + " ");
            }
            System.out.println();
        }
    }

    public static void similarity2Rating(SimilarityMatrix similarityMatrix){
        for (int user = 1; user <= NUM_USER; user++){
            for (int movie = 1; movie <= TITLES;movie++){
                similarityMatrix.estimateRating(user,movie);
            }
        }
    }

    public static void rateMSE(boolean doTrainNotTest){
        double[] MSEs = new double[5];
        int loop = 0;
        for (int users = 100; users <= MAX_USER;users *= 2){
            //1.trainを読み込んで類似度行列作成
            SimilarityMatrix similarityMatrix3 = new SimilarityMatrix(users,doTrainNotTest);
            file2Matrix(decideFilePathTest(users),similarityMatrix3);

            //2.testを読み込んで推定
            RatingMatrix testRatingMatrix = new RatingMatrix();
            testRatingMatrix.loadData(decideFilePathTest(users));
            similarityMatrix3.calcMSE(testRatingMatrix);

            //3.評価
            MSEs[loop] = similarityMatrix3.getMSE();

            loop++;
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

            int user1 = 1;
            while((readString = in.readLine()) != null) {
                strings = readString.split(SPLITTER);
                for (int user2 = 1; user2 <= NUM_USER;user2++){
                    similarityMatrix2.setSimilarity(user1,user2,Double.parseDouble(strings[user2-1]));
                }
                user1++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String decideFilePathTest(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2TEST;
    }


}
