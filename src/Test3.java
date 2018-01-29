/**
 * Created by Luis on 2018/01/23.
 */
public class Test3 {
    private static final String SPLITTER = ",";
    private static final String FILENAME = "result.csv";
    private static final int NUM_USER = 116;
    private static final String[] RESTAURANTS =
            {"油そば  油虎","創作中華料理  えん弥","東京背脂  銀の豚","お好み焼き  三八", "七福軒","清六家","カレーうどん ＺＥＹＯ",
                    "千香華味", "活龍まぜそば専門  天地","鶏々","ＤＯＬＦ","定食・居酒屋  どっとくう","百香亭","めしや  益さん",
                    "夢屋","お好み焼き  より味ち","海の幸  わだつみ","中華定食  笑飯店","中華料理北方園","つけめん・らーめん活龍",
                    "ジュエル・オブ・インディア","麺や  小五郎","薔薇絵亭","くい亭","クラレット"};
    private static final int NUM_MY_ANSWER = 3;

    public static void main(String[] args){
        int num_restaurant = RESTAURANTS.length;
        //System.out.println(RESTAURANTS.length);

        SimilarityMatrix similarityMatrix = new SimilarityMatrix(NUM_USER,FILENAME,num_restaurant);
        String myAverageRating = String.format("%.2f",similarityMatrix.getAUserAverageRating(NUM_MY_ANSWER));

        double MSE = 0.0;
        int countMSE = 0;

        //System.out.println("(推定評価値,実際の評価値)の一覧");
        for (int restaurant = 1; restaurant <= num_restaurant;restaurant++){
            similarityMatrix.estimateRating(NUM_MY_ANSWER,restaurant);
            String tmpEstimatedRating = String.format("%.2f",similarityMatrix.getEstimatedRating(NUM_MY_ANSWER,restaurant));
            String tmpRestaurant = RESTAURANTS[restaurant-1];
            int tmpLength = 15 - tmpRestaurant.length();
            int tmpRealRating = similarityMatrix.getRealRating(NUM_MY_ANSWER,restaurant);

            System.out.print(tmpRestaurant);
            for (int i = 0;i < tmpLength;i++){
                System.out.print("  ");
            }
            if (tmpRealRating != 0){
                System.out.println("-> " + "(" + tmpEstimatedRating + "," + tmpRealRating + ")");
                MSE += (tmpRealRating - Double.parseDouble(tmpEstimatedRating)) * (tmpRealRating - Double.parseDouble(tmpEstimatedRating));
                countMSE++;
            } else {
                System.out.println("-> " + "(" + tmpEstimatedRating + ",評価なし)");
            }

        }
        System.out.println("\n私の平均評価値 -> " + myAverageRating);
        System.out.println("実際の評価値があるデータ17個のみでのMSE -> " + String.valueOf(Math.sqrt(MSE/countMSE)));








        //入力を受け付けてそれを追加して…と真面目にやろうとしてたのがこの下//
/*        BufferedReader in = null;
        BufferedReader input = null;
        String question = "お店が好きであればあるほど5に近い数字を入力、" +
                          "好きでなければないほど1に近い数字を入力、" +
                          "行ったことがないor覚えてない場合は何も入力せずにエンターを押してください";
        String readString;
        String[] tmpStrings;

        try{
            in = new BufferedReader(new FileReader(FILENAME));
            input = new BufferedReader(new InputStreamReader(System.in));

            //指定したファイルの内容を読み込む準備
            readString = in.readLine();
            tmpStrings = readString.split(SPLITTER);
            int num_item = tmpStrings.length;

            //今回の標準入力で受け付けて追加するデータ
            System.out.println(num_item + "個のお店の評価データを入力していただきます");
            int[] appendData = new int[tmpStrings.length];

            //標準入力から追加するデータを収集
            System.out.println(question);
            int i = 0;
            for (String restaurant : tmpStrings){
                System.out.print(restaurant + "の評価は？ ");
                appendData[i] = Integer.parseInt(input.readLine());
                i++;
            }
            input.close();

            //収集したデータを追加
            FileWriter fw = new FileWriter(FILENAME,true);
            for (int element : appendData){
                fw.write(element + ",");
            }
            fw.write("\n");
            fw.close();

            //ユーザの評価したデータを読み込んでいく
            int userNum = 0;
            while ((readString = in.readLine()) != null){
                tmpStrings = readString.split(SPLITTER);

                userNum++;
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
        }*/
    }
}
