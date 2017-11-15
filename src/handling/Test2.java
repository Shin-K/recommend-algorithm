package handling;

import java.io.*;

/**
 * Created by Luis on 2017/11/15.
 */
public class Test2 {
    private static final int TITLES = 3952;
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2 = "User_train_ratings.dat";
    private static final String fileName = "100User_similarities.dat";
    private static BufferedReader in = null;
    private static final String SPLITTER = ",";
    private static final int num_user = 100;



    public static void main(String[] args){
        //課題2-1
        SimilarityMatrix similarityMatrix = new SimilarityMatrix(num_user);
        int user1,user2;
        for (user1 = 1; user1 <= num_user; user1++){
            for (user2 = 1; user2 <= num_user; user2++){
                similarityMatrix.calcAndSetSimilarity(user1,user2);
            }
        }
        matrix2File(num_user,similarityMatrix);

        SimilarityMatrix similarityMatrix2 = new SimilarityMatrix(num_user); //出力用の行列
        file2Matrix(fileName,similarityMatrix2);


    }

    public static void matrix2File(int num_user,SimilarityMatrix similarityMatrix) {
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


    public static void file2Matrix(String fileName, SimilarityMatrix similarityMatrix2){
        try {
            String[] strings;
            String readString;
            in = new BufferedReader(new FileReader(fileName)); //読み込むファイルをパスで指定

            int user1 = 1;
            while((readString = in.readLine()) != null) {
                strings = readString.split(SPLITTER);
                for (int user2 = 1; user2 <= num_user;user2++){
                    similarityMatrix2.setSimilarity(user1,user2,Double.parseDouble(strings[user2-1]));
                }
                user1++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static String decideFilePath(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2;
    }

}
