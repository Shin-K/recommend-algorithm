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



    public static void main(String[] args){
        boolean doTrainNotTest = true;
        String readString;
        String[] tmpData;
        int atUser, atMovie;
        SimilarityMatrix similarityMatrix = new SimilarityMatrix(NUM_USER,doTrainNotTest);

        //課題2-1 & レポート2-2-1
        printSimilarityMatrix(similarityMatrix);

        try{
            in = new BufferedReader(new FileReader(decideFilePathTest(NUM_USER))); //読み込むファイルをパスで指定

            while((readString = in.readLine()) != null) {
                tmpData = readString.split(SPLITTER);
                atUser = Integer.parseInt(tmpData[0]);
                atMovie = Integer.parseInt(tmpData[1]);

                similarityMatrix.estimateRating(atUser,atMovie);
                double tmp = similarityMatrix.getEstimatedRating(atUser,atMovie);
                System.out.println("user" + atUser + "の" + "movie" + atMovie + "への推定評価値 -> " + tmp);

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

        //matrix2File(NUM_USER,similarityMatrix);
        //SimilarityMatrix similarityMatrix2 = new SimilarityMatrix(NUM_USER,doTrainNotTest); //出力用の行列
        //file2Matrix(fileName,similarityMatrix2);


        //レポート2-2-2
        //similarity2Rating(similarityMatrix);

        //課題2-2
        //similarity2Rating(similarityMatrix);

        //課題2-3
        double[] MSEs = new double[5];
        int loop = 0;
        int users = 10;
        //for (int users = 10; users <= 10;users *= 2){
            MSEs[loop] = rateMSE(doTrainNotTest,users);
            loop++;
            //System.out.println(MSEs[loop]);
        //}


    }

    public static void printSimilarityMatrix(SimilarityMatrix similarityMatrix){
        int user1,user2;
        System.out.println("       1     2     3     4     5     6     7     8     9    10");
        for (user1 = 1; user1 <= NUM_USER; user1++){
            if (user1 != 10) System.out.print(user1 + " ");
            else System.out.print(user1);

            for (user2 = 1; user2 <= NUM_USER; user2++){
                similarityMatrix.calcAndSetSimilarity(user1,user2);
                double tmp = round(similarityMatrix.getSimilarity(user1,user2));
                if (tmp < 0) System.out.print(" " + String.format("%.2f",tmp));
                else System.out.print("  " + String.format("%.2f",tmp));
            }
            System.out.println();
        }
    }

    public static void similarity2Rating(SimilarityMatrix similarityMatrix){
        for (int user = 1; user <= NUM_USER; user++){
            //以下はprintするときのみ
            System.out.println("user" + user + "の推定評価値リスト");
            for (int movie = 1; movie <= 10;movie++){
                similarityMatrix.estimateRating(user,movie);

                //以下はprintするときのみ
                double tmp = similarityMatrix.getEstimatedRating(user,movie);
                if (movie % 5 == 0) System.out.println("movie" + movie + " -> " + String.format("%.2f",tmp));
                else System.out.print("movie" + movie + " -> " + String.format("%.2f",tmp) + "  ");
            }
            //以下はprintするときのみ
            System.out.println();
        }
    }

    public static double rateMSE(boolean doTrainNotTest, int users){
        //1.trainを読み込んで類似度行列作成
        SimilarityMatrix similarityMatrix3 = new SimilarityMatrix(users, doTrainNotTest);
        file2Matrix(decideFilePathTest(users), similarityMatrix3);

        //2.testを読み込んで推定
        RatingMatrix testRatingMatrix = new RatingMatrix();
        testRatingMatrix.loadData(decideFilePathTest(users));
        similarityMatrix3.calcMSE(testRatingMatrix);

        //3.評価
        return similarityMatrix3.getMSE();
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
