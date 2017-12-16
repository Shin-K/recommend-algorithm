import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Luis on 2017/10/25.
 */
public class Test1_3 {
    private static final String FILE_PATH1 = "res/exp_data/";
    private static final String FILE_PATH2 = "User_train_ratings.dat";
    private static final int TITLES = 3952;
    private static final int movie1 = 5;
    private static final int movie2 = 10;
    private static final int user1 = 1;
    private static final int user2 = 5;
    private static final boolean printTitleTable = true;
    private static final int COUNT = 1000;
    private static final int NUM_USER = 1600;
    private static long sumCalcTime = 0;
    private static String user2List1 = "人の時、任意の二つのUserIDを入力に取り、";
    private static String user2List2 = "両方のユーザが見た映画のMovieIdとその映画のRatingのリストを出力する平均時間 : ";
    private static String movie2List1 = "人の時、任意の二つのMovieIDを入力に取り、";
    private static String movie2List2 = "両方の映画を見たユーザのUserIdとそのユーザが与えたRatingのリストを出力する平均時間 : ";

    private static final boolean movie2UserFlag = false;
    private static final boolean isChangeN = true;
    private static final boolean isChangeM = false;




    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        RatingMatrix ratingMatrix = new RatingMatrix();
        String fileName = "CalculateTime1-3-5-4.csv";
        //try-catchでユーザー数を変化させて、結果をcsv書き込み、要領はTest1-2と一緒

        try {
            Random rand = new Random();
            rand.setSeed(100);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));


            if (isChangeN){
                //N変化させる時
                for (int number = 100; number <= 100; number = number*2){
                    ratingMatrix.loadData(decideFilePath(number));
                    sumCalcTime = 0;
                    for (int i = 0;i < COUNT;i++) {
                        //measureCalculateTimeFromId(list,ratingMatrix,rand,number); //課題1-3の1と2
                        measureCalculateTimeFrom2Ids(list,ratingMatrix,rand,number); //課題1-3の3と4
                    }
                    double time = sumCalcTime / COUNT;
                    //ユーザー数、そのユーザー数での平均計測時間のcsv書き出し
                    pw.print(String.valueOf(number));
                    pw.print(",");
                    pw.println(String.valueOf(time));
                }
            }

            if (isChangeM) {
                //M変化させる時
                for (int num_movie = 1; num_movie <= TITLES;num_movie++) {
                    if (num_movie % 100 != 0) continue;

                    ratingMatrix.loadData(decideFilePath(NUM_USER),num_movie);
                    System.out.println(num_movie);
                    sumCalcTime = 0;

                    for (int i = 0; i < COUNT;i++){
                        measureCalculateTimeFromId(list,ratingMatrix,rand,num_movie); //課題1-3の1と2
                        //measureCalculateTimeFrom2Ids(list,ratingMatrix,rand,num_movie); //課題1-3の3と4
                        if (i % 200 == 0) System.out.print("- ");
                    }
                    System.out.println("calc fin");
                    double time = sumCalcTime / COUNT;
                    //ユーザー数、そのユーザー数での平均計測時間のcsv書き出し
                    pw.print(String.valueOf(num_movie));
                    pw.print(",");
                    pw.println(String.valueOf(time));
                }
            }

            pw.close();
        } catch (IOException e){
            e.printStackTrace();
        }


        //ratingMatrix.printUserRatingList(movie1);
        //ratingMatrix.printMovieRatingList(user1);

/*        if (movie2UserFlag) {
            System.out.println(USERNUM + movie2List1);
            System.out.println(movie2List2 + sumCalcTime/COUNT + "(nanosec)");
        }
        else {
            System.out.println(USERNUM + user2List1);
            System.out.println(user2List2 + sumCalcTime/COUNT + "(nanosec)");
        }*/


        //ratingMatrix.printMovieRatingListFrom2Users(list,printTitleTable);
        //ratingMatrix.printUserRatingListFrom2Movies(movie1,movie2,printTitleTable);

    }

    public static void measureCalculateTimeFromId(List<Integer> list, RatingMatrix ratingMatrix, Random rand, int limit){

        int id;
        if (movie2UserFlag && isChangeM || !movie2UserFlag && isChangeN) {
            id = rand.nextInt(limit) + 1;
        }
        else if (movie2UserFlag) {
            id = rand.nextInt(TITLES) + 1;
        }
        else {
            id = rand.nextInt(NUM_USER) + 1;
        }

        long start = System.nanoTime();
        list = ratingMatrix.makeListFromId(id,list,movie2UserFlag);
        long end = System.nanoTime();
        sumCalcTime += end - start;
    }

    public static void measureCalculateTimeFrom2Ids(List<Integer> list, RatingMatrix ratingMatrix, Random rand, int limit){

            int id1,id2;
            if (movie2UserFlag && isChangeM || !movie2UserFlag && isChangeN) {
                id1 = rand.nextInt(limit) + 1;
                id2 = rand.nextInt(limit) + 1;
            }
            else if (movie2UserFlag) {
                id1 = rand.nextInt(TITLES) + 1;
                id2 = rand.nextInt(TITLES) + 1;
            }
            else {
                id1 = rand.nextInt(NUM_USER) + 1;
                id2 = rand.nextInt(NUM_USER) + 1;
            }

            long start = System.nanoTime();
            list = ratingMatrix.makeListFrom2Ids(id1,id2,list,movie2UserFlag);
            for (int i : list){
                System.out.println(i);
            }
            long end = System.nanoTime();
            sumCalcTime += end - start;
    }


    public static String decideFilePath(int userNum){
        return FILE_PATH1 + String.valueOf(userNum) + FILE_PATH2;
    }
}
