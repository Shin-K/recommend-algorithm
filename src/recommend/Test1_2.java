package recommend;

import recommend.MovieTitle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Luis on 2017/10/18.
 */
public class Test1_2 {
        public static void main(String[] args) {
            final int N = 3952; //タイトル数
            final double count = 10000; //ループ回数
            String fileName = "calculateTime.csv";

            MovieTitle movieTitle = new MovieTitle();
            movieTitle.loadTitles();

            try{
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

                //titleNumbersToGetの値の数だけタイトルを取ってくる、それをcountの数だけループ回して繰り返す
                //HashMapのputのオーダーはO(1)なので、タイトル数によらず平均計算時間は一定になるはず
                for (int titleNumbersToGet = 1;titleNumbersToGet <= N;titleNumbersToGet++){
                    long sumCalcTime = 0;
                    long start = System.nanoTime();
                    for (int j = 0; j < count; j++) {
                        for (int getTitle = 0; getTitle < titleNumbersToGet;getTitle++) {
                            //movieTitle.printTable(getTitle);
                        }
                    }
                    long end = System.nanoTime();
                    sumCalcTime += end - start;

                    //読み込むタイトル数が100ごとにその平均計算時間をファイルに書き込み、csvファイルとして出力
                    double time = sumCalcTime / count;
                    if (titleNumbersToGet % 100 == 0){
                        //System.out.println("Calculate time : " + titleNumbersToGet + " titles -> " + time/titleNumbersToGet + " nanosec");
                        pw.print(String.valueOf(titleNumbersToGet));
                        pw.print(",");
                        pw.println(String.valueOf(time/titleNumbersToGet));
                    }
                }
                pw.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }
}
