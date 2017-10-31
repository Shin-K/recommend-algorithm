package handling;

/**
 * Created by Luis on 2017/10/25.
 */
public class IdAndRating implements Comparable<IdAndRating> {

    public IdAndRating(int id,int rating1){
        this.id = id;
        this.rating1 = rating1;
    }

    public IdAndRating(int id, int rating1,int rating2){
        this.id = id;
        this.rating1 = rating1;
        this.rating2 = rating2;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRatingUser1() {
        return rating1;
    }


    public int getRatingUser2() {
        return rating2;
    }

    private int id;
    private int rating1;
    private int rating2;

    @Override
    public int compareTo(IdAndRating o) {
        return this.getId() - o.getId();
    }
}
