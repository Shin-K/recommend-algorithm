package handling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Luis on 2017/10/20.
 */
public class RatingMatrix {
    private BufferedReader in = null;
    private final String SPLITTER = "::";
    private String[] tempData;
    private String readString;
    private int atUser,atMovie,atRating;

    private static Map<Integer,Map<Integer,Integer>> ratingMatrix = new HashMap<>();

    private static Map<Integer,Set<Integer>> movieId2User = new HashMap<>();
    private static Map<Integer,Set<Integer>> userId2Movie = new HashMap<>();


    public void loadData(String filePath){
        try{
            in = new BufferedReader(new FileReader(filePath)); //読み込むファイルをパスで指定

            while((readString = in.readLine()) != null) {
                tempData = readString.split(SPLITTER);
                atUser = Integer.parseInt(tempData[0]);
                atMovie = Integer.parseInt(tempData[1]);
                atRating = Integer.parseInt(tempData[2]);
                putData(atUser,atMovie,atRating);
                putMovieId2User(atMovie,atUser);
                putUserId2Movie(atUser,atMovie);
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


    public static int getRating(int userId, int movieId){
        return ratingMatrix.get(userId).get(movieId);
    }

    public static void putData(int userId, int movieId, int rating){
        if (!ratingMatrix.containsKey(userId)) ratingMatrix.put(userId,new HashMap<>());
        ratingMatrix.get(userId).put(movieId,rating);
        //System.out.println(ratingMatrix.toString());
    }


    public static void putMovieId2User(int movieId,int userId){
        if(!movieId2User.containsKey(movieId)) movieId2User.put(movieId,new TreeSet<>());
        movieId2User.get(movieId).add(userId);
        //System.out.println(movieId2User.toString());
    }

    public static void putUserId2Movie(int userId,int movieId){
        if(!userId2Movie.containsKey(userId)) userId2Movie.put(userId,new TreeSet<>());
        userId2Movie.get(userId).add(movieId);
        //System.out.println(userId2Movie.toString());
    }

/*    public static Iterator makeIterator(Map map,int id){
        if (!map.containsKey(id)) return null;
        return map.get(id).iterator();
    }*/

    public static void printUserRatingList(int movieId){
        if (!movieId2User.containsKey(movieId)) return;

        Iterator<Integer> userIds = movieId2User.get(movieId).iterator();
        System.out.println("〜No." + movieId + "の映画を観たユーザーとそのユーザーによる評価〜");
        System.out.println("ユーザーID     評価値");
        while (userIds.hasNext()){
            int tmpUserId = userIds.next();
            int rating = getRating(tmpUserId,movieId);
            System.out.printf("%-10d    %-10d\n", tmpUserId, rating);
        }
    }

    public static void printMovieRatingList(int userId){
        if (!userId2Movie.containsKey(userId)) return;

        Iterator<Integer> movieIds = userId2Movie.get(userId).iterator();
        System.out.println("〜No." + userId + "ユーザーが観た映画とその評価〜");
        System.out.println("映画ID     評価値");
        while (movieIds.hasNext()){
            int tmpMovieId = movieIds.next();
            int rating = getRating(userId,tmpMovieId);
            System.out.printf("%-10d    %-10d\n", tmpMovieId, rating);
        }
    }

/*    public static void printFunction(int arg, Iterator<Integer> id){
        while (id.hasNext()){
            int tmpId = id.next();
            int rating = getRating(arg,tmpId);
            System.out.printf("%-10d    %-10d", tmpId, rating);
            System.out.println();
        }
    }*/

    public static void printMovieRatingListFrom2Users(int user1, int user2, boolean printTitleTable){
        if (!userId2Movie.containsKey(user1) || !userId2Movie.containsKey(user2)) {
            System.out.println("ユーザーNo." + user1 + "とユーザーNo." + user2 + "の両者とも観た映画はありません。");
            return;
        } else {
            Set<Integer> intersection = new TreeSet<>(userId2Movie.get(user1));
            intersection.retainAll(userId2Movie.get(user2));
            if (intersection.size() == 0) {
                System.out.println("ユーザーNo." + user1 + "とユーザーNo." + user2 + "の両者とも観た映画はありません。");
                return;
            }
        }
/*        if (!userId2Movie.containsKey(user1) || !userId2Movie.containsKey(user2)) {
            System.out.println(user1 + "と" + user2 + "が両者とも観た映画はありません。");
            return;
        }*/

        MovieTitle movieTitle = new MovieTitle();
        if (printTitleTable){
            movieTitle.loadTitles();
            System.out.println("〜No." + user1 + " & No." + user2 + "ユーザーが観た映画タイトル一覧〜");
            System.out.println("映画ID    タイトル");
        } else {
            System.out.println("〜No." + user1 + " & No." + user2 + "ユーザーが観た映画とその評価〜");
            System.out.println("映画ID    No." + user1 + "ユーザーの評価値    No." + user2 + "ユーザーの評価値");
        }


        Iterator<Integer> movieIds = userId2Movie.get(user1).iterator();

        while (movieIds.hasNext()){
            int tmpMovieId = movieIds.next();
            if (movieId2User.get(tmpMovieId).contains(user2)){
                int rating1 = getRating(user1,tmpMovieId);
                int rating2 = getRating(user2,tmpMovieId);

                if (printTitleTable) System.out.printf("%-5d    %-5s\n", tmpMovieId, movieTitle.getTitle(tmpMovieId));
                else System.out.printf("%-12d    %-10d    %10d\n", tmpMovieId, rating1,rating2);
            }

        }
    }

    public static void printUserRatingListFrom2Movies(int movie1, int movie2, boolean printTitleTable){
        if (!movieId2User.containsKey(movie1) || !movieId2User.containsKey(movie2)) {
            System.out.println("No." + movie1 + "映画とNo." + movie2 + "映画の2つとも観たユーザーはいません。");
            return;
        } else {
            Set<Integer> intersection = new TreeSet<>(movieId2User.get(movie1));
            intersection.retainAll(movieId2User.get(movie2));
            if (intersection.size() == 0) {
                System.out.println("No." + movie1 + "映画とNo." + movie2 + "映画の2つとも観たユーザーはいません。");
                return;
            }
        }
        //if (!movieId2User.containsKey(movie1)) return;
        //if (!movieId2User.containsKey(movie2)) return;

        MovieTitle movieTitle = new MovieTitle();
        if (printTitleTable){
            movieTitle.loadTitles();
            System.out.println("〜No." + movie1 + " & No." + movie2 + "映画のタイトルとそれを視聴したユーザーの一覧〜");
            System.out.println("タイトル : " + movieTitle.getTitle(movie1) + ", " + movieTitle.getTitle(movie2));
            System.out.print("ユーザーID : ");
        } else {
            System.out.println("〜No." + movie1 + " & No." + movie2 + "映画を観たユーザーと彼らによる評価〜");
            System.out.println("ユーザーID    No." + movie1 + "映画の評価値    No." + movie2 + "映画の評価値");
        }

        Iterator<Integer> userIds = movieId2User.get(movie1).iterator();

        while (userIds.hasNext()){
            int tmpUserId = userIds.next();
            if (userId2Movie.get(tmpUserId).contains(movie2)){
                int rating1 = getRating(tmpUserId,movie1);
                int rating2 = getRating(tmpUserId,movie2);
                if (printTitleTable) {
                    System.out.print(tmpUserId + " ");
                }
                else System.out.printf("%-12d    %-10d    %10d\n", tmpUserId, rating1,rating2);
            }

        }
    }

}
