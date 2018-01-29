import java.io.*;

/**
 * Created by Luis on 2017/11/09.
 */
public class Test1_4 {
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2 = "User_train_ratings.dat";
    private static final int TITLES = 3952;
    private static final int USERNUM = 1600;
    private static final boolean movie2UserFlag = false;
    private static final String fileName = "1-4.csv";
    private static BufferedReader in = null;
    private static final String SPLITTER = ",";
    private static String readString;

    private static int[][] S = new int[USERNUM][TITLES];
    private static int[][] output = new int[USERNUM][TITLES];


    public static void main(String[] args){
        int number = 1600;

        RatingMatrix ratingMatrix = new RatingMatrix();
        ratingMatrix.loadData(decideFilePath(number));

        //array2File(number,ratingMatrix);
        file2Array();

    }

    public static void array2File(int number, RatingMatrix ratingMatrix) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

            for (int i = 1; i <= number;i++){
                for (int j = 1; j <= TITLES; j++){
                    if(ratingMatrix.checkIdExists(i,j,movie2UserFlag)) S[i-1][j-1] = ratingMatrix.getRating(i,j,movie2UserFlag);
                    else S[i-1][j-1] = 0;

                    pw.print(String.valueOf(S[i-1][j-1]) + ",");
                    //pw.print(SPLITTER);
                }
                pw.println();
            }
            pw.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void file2Array(){
        try {
            String[] strings;
            in = new BufferedReader(new FileReader(fileName)); //読み込むファイルをパスで指定

            int i = 0;
            while((readString = in.readLine()) != null) {
                strings = readString.split(SPLITTER);
                for (int j=0; j < TITLES;j++){
                    output[i][j] = Integer.parseInt(strings[j]);
                    //System.out.print(output[i][j] + " ");
                }
                //System.out.println();
                i++;
            }
            //System.out.println("debug");

            } catch (IOException e){
            e.printStackTrace();
        }


    }

    public static String decideFilePath(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2;
    }

}
