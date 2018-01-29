/**
 * Created by Luis on 2017/10/25.
 */
public class Report1_3_2and3 {

        private static final String filePath = "res/exp_data/10User_train_ratings.dat";
        private static final int TITLES = 3952;
        private static final int movie1 = 1;
        private static final int movie2 = 5;
        private static final int user1 = 1;
        private static final int user2 = 5;
        private static final boolean printTitleTable = true;

        public static void main(String[] args){
            RatingMatrix0 ratingMatrix0 = new RatingMatrix0();
            ratingMatrix0.loadData(filePath);
            //ratingMatrix.printUserRatingList(movie1);
            //ratingMatrix.printMovieRatingList(user1);
            //ratingMatrix0.printMovieRatingListFrom2Users(user1,user2,printTitleTable);
            ratingMatrix0.printUserRatingListFrom2Movies(movie1,movie2,printTitleTable);

        }

}
