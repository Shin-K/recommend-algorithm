import java.io.BufferedReader;
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

    public Map<Integer,Set<Integer>> getUserId2Movie(){
        return userId2Movie;
    }

    public Set<Map.Entry<Integer,Map<Integer,Integer>>> makeEntrySet(){
        return ratingMatrix.entrySet();
    }


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

    //アイテム数を変化させる時は、movieIdの値が一定以下の時のみデータをputする
    public void loadData(String filePath,int num_movie){
        try{
            in = new BufferedReader(new FileReader(filePath)); //読み込むファイルをパスで指定

            while((readString = in.readLine()) != null) {
                tempData = readString.split(SPLITTER);
                atUser = Integer.parseInt(tempData[0]);
                atMovie = Integer.parseInt(tempData[1]);
                atRating = Integer.parseInt(tempData[2]);
                if (atMovie <= num_movie){
                    putData(atUser,atMovie,atRating);
                    putMovieId2User(atMovie,atUser);
                    putUserId2Movie(atUser,atMovie);
                    //System.out.println(atUser + " " + atMovie);
                }
            }
            //System.out.println("load fin");
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

    public boolean checkIdExists(int id1, int id2, boolean movie2UserFlag){
        if (movie2UserFlag){
            if (ratingMatrix.containsKey(id2)){
                if (ratingMatrix.get(id2).containsKey(id1)) return true;
            }
        }
        else {
            if (ratingMatrix.containsKey(id1)){
                if (ratingMatrix.get(id1).containsKey(id2)) return true;
            }
        }
        return false;
    }


    public int getRating(int id1, int id2, boolean movie2UserFlag){
        //System.out.println(id1 + " " + id2 + " " + String.valueOf(movie2UserFlag));
        if (movie2UserFlag) return ratingMatrix.get(id2).get(id1);
        else return ratingMatrix.get(id1).get(id2);

    }

    public void putData(int userId, int movieId, int rating){
        //if (userId == 2 && movieId == 7) System.out.println("debug");
        if (!ratingMatrix.containsKey(userId)) ratingMatrix.put(userId,new HashMap<>());
        ratingMatrix.get(userId).put(movieId,rating);
        //System.out.println(ratingMatrix.toString());
    }


    public void putMovieId2User(int movieId,int userId){
        if(!movieId2User.containsKey(movieId)) movieId2User.put(movieId,new TreeSet<>());
        movieId2User.get(movieId).add(userId);
        //System.out.println(movieId2User.toString());
    }

    public void putUserId2Movie(int userId,int movieId){
        if(!userId2Movie.containsKey(userId)) userId2Movie.put(userId,new TreeSet<>());
        userId2Movie.get(userId).add(movieId);
        //System.out.println(userId2Movie.toString());
    }

/*    public static Iterator makeIterator(Map map,int id){
        if (!map.containsKey(id)) return null;
        return map.get(id).iterator();
    }*/

/*    public static void printUserRatingList(int movieId){
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
    }*/

/*    public static void printFunction(int arg, Iterator<Integer> id){
        while (id.hasNext()){
            int tmpId = id.next();
            int rating = getRating(arg,tmpId);
            System.out.printf("%-10d    %-10d", tmpId, rating);
            System.out.println();
        }
    }*/

    public List<Integer> makeListFromId(int id, List<Integer> list, boolean movie2UserFlag) {

        Map<Integer, Set<Integer>> id2Set;
        if (movie2UserFlag) id2Set = movieId2User;
        else id2Set = userId2Movie;

        if (!id2Set.containsKey(id)) {
            //System.out.println("ユーザーNo." + user1 + "とユーザーNo." + user2 + "の両者とも観た映画はありません。");
            return list;
        } else {
            Set<Integer> valueMap = new TreeSet<>(id2Set.get(id));

            for (int valueId : valueMap) {
                //IdAndRating one = new IdAndRating(valueId, getRating(id, valueId, movie2UserFlag));
                //System.out.println(id + "," + valueId + " : " + getRating(id,valueId,movie2UserFlag));
                list.add(valueId);
            }
            Collections.sort(list);

            return list;

        }
    }

    public List<Integer> makeListFrom2Ids(int id1, int id2, List<Integer> list, boolean movie2UserFlag){

        Map<Integer,Set<Integer>> id2Set;
        if (movie2UserFlag) id2Set = movieId2User;
        else id2Set = userId2Movie;

        if (!id2Set.containsKey(id1) || !id2Set.containsKey(id2)) {
            //System.out.println("ユーザーNo." + user1 + "とユーザーNo." + user2 + "の両者とも観た映画はありません。");
            return list;
        } else {
            Set<Integer> intersection = new TreeSet<>(id2Set.get(id1));
            intersection.retainAll(id2Set.get(id2));

            for (int anId : intersection){
                //if (id1 == 1 && id2 == 2) System.out.println(anId);
                //IdAndRating one = new IdAndRating(anId,getRating(id1,anId,movie2UserFlag),getRating(id2,anId,movie2UserFlag));
                //System.out.println(one.getId());
                list.add(anId);
            }
            Collections.sort(list);

            return list;

        }
/*        if (!userId2Movie.containsKey(user1) || !userId2Movie.containsKey(user2)) {
            System.out.println(user1 + "と" + user2 + "が両者とも観た映画はありません。");
            return;
        }*/
    }

/*    public static void printMovieRatingListFrom2Users(List<IdAndRating> list, boolean printTitleTable){
        MovieTitle movieTitle = new MovieTitle();
        Iterator<IdAndRating> iterator = list.iterator();


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
    }*/

/*    public static void printUserRatingListFrom2Movies(int movie1, int movie2, boolean printTitleTable){
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
    }*/

}
