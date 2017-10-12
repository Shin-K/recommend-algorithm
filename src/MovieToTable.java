//課題1-1

import java.io.*;


public class MovieToTable {
    private static final String SPLITTER = "::";
    private static final int USERNUM = 10;

    static int[][] ratingsTable = new int[USERNUM][USERNUM];



    public static void readRatings(String filePath){

    }



    public static void main(String[] args) {
        String filePath = args[0];
        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(filePath));
            String readString;
            while((readString = in.readLine()) != null){
                System.out.println(readString);
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




