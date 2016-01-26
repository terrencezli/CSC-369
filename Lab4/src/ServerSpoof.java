import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by terrence on 1/26/16.
 */
public class ServerSpoof {


    public static void main(String args[]) {
        MongoClient c;
        MongoDatabase db;
        JSONTokener t;
        JSONObject js;
        Scanner s;
        ArrayList<String> list;

        try {
            t = new JSONTokener(new FileReader(new File(args[0])));
            js = new JSONObject(t);
            c = new MongoClient(js.getString("mongo"));  // connect to server
            db = c.getDatabase(js.getString("database"));    // grab database called "alex"

            String collectionName = js.getString("collection");
            System.out.println("number of Documents:" + db.getCollection(collectionName).count());
            db.getCollection(js.getString("monitor")).drop();

            s = new Scanner(new File(js.getString("wordFilter")));
            list = new ArrayList<String>();
            while (s.hasNext()){
                list.add(s.next());
            }
            s.close();

            System.out.println("===XXXXXXXXXX==========");
            Date date= new Date();
            System.out.println(new Timestamp(date.getTime()));
            System.out.println("database name:" + db.getName());
            System.out.println("collection:" + collectionName);
            System.out.println("number of Documents:" + db.getCollection(collectionName).count());

            while (true) {
                JSONObject monitor = new JSONObject();
                TimeUnit.SECONDS.sleep(3 * js.getInt("delay"));

                date= new Date();
                monitor.put("timestamp",new Timestamp(date.getTime()));

                MongoCursor<String> distMessages= db.getCollection(collectionName).distinct("text", String.class).iterator();

                int uniqueMessages = 0;
                while (distMessages.hasNext()) {
                    uniqueMessages++;
                    distMessages.next();
                }

                MongoCursor<String> distUsers= db.getCollection(collectionName).distinct("user", String.class).iterator();

                int uniqueUsers = 0;
                while (distUsers.hasNext()) {
                    uniqueUsers++;
                    distUsers.next();
                }



            }

        } catch (FileNotFoundException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }
}
