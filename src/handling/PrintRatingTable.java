package handling;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Luis on 2017/10/13.
 */

//ほんとは直接出力するのは良くなくて、一旦どこかに保存してそれをプリントすべき

public class PrintRatingTable {

    public static void main(String[] args) {
        final String SPLITTER = "::";
        String filePath = args[0];
        BufferedReader in = null;
        String[] tempData;
        int countUser = 1,countMovie = 1;
        int atUser,atMovie,atRating;
        String readString;
        String movieNumbers = "   1 2 3 4 5 6 7 8 9 10";

        try{
            in = new BufferedReader(new FileReader(filePath)); //読み込むファイルをパスで指定

            System.out.println(movieNumbers); //表の1行目
            System.out.print(String.valueOf(countUser) + " "); //ユーザー1だけ先にプリント

            while((readString = in.readLine()) != null) {
                //読み込んだファイルの各行からユーザー、映画、その評価を取り出して分けておく
                tempData = readString.split(SPLITTER);
                atUser = Integer.parseInt(tempData[0]);
                atMovie = Integer.parseInt(tempData[1]);
                atRating = Integer.parseInt(tempData[2]);

                //表で次のユーザーに移る時の処理
                if(atUser != countUser && countMovie == 11){
                    System.out.println();
                    countMovie = 1;
                    countUser++;
                    if(countUser==10) System.out.print(String.valueOf(countUser) + "");
                    else System.out.print(String.valueOf(countUser) + " ");
                }

                //今注目しているユーザー&映画のところに行き着くまでは（評価データがないところは）-を挿入し
                //注目しているところまでそれを繰り返す
                while (atMovie > countMovie){
                    System.out.print(" -");
                    countMovie++;
                }
                System.out.print(" " + String.valueOf(atRating)); //該当箇所の評価を書き込む
                countMovie++;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
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
}
