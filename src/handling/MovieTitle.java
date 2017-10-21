package handling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luis on 2017/10/17.
 */
public class MovieTitle {
    Map<Integer,String> movieTitles = new HashMap<>();
    String filePath = "res/exp_data/movies.dat";
    String splitter = "::";


    public void loadTitles(){
        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(filePath));
            String readString;

            //1行ずつ読み込み、::で分割、IDとタイトルを取り出してmovieTitlesにput
            while ((readString = in.readLine()) != null){
                String[] strings = readString.split(splitter);
                int movieId = Integer.parseInt(strings[0]);
                String title = strings[1];
                movieTitles.put(movieId,title);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try{
                    in.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public  String getTitle(int movieId){
        return movieTitles.get(movieId);
    }

    public void printTable(int index){
        //getTitle(index + 1);
        System.out.println(String.valueOf(index + 1) +  " : " + getTitle(index + 1));
    }

}
