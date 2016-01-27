import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import org.bson.Document;
import org.json.JSONArray;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

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
            db = c.getDatabase(js.getString("database"));

            Logger logger = Logger.getLogger("org.mongodb.driver");  // turn off logging
            logger.setLevel(Level.OFF);                              // this lets us squash a lot

            String collectionName = js.getString("collection");
            System.out.println("number of Documents:" + db.getCollection(collectionName).count());

            // drops collection
            db.getCollection(js.getString("monitor")).drop();

//            s = new Scanner(new File(js.getString("wordFilter")));
//            list = new ArrayList<String>();
//            while (s.hasNext()){
//                list.add(s.next());
//            }
//            s.close();

            System.out.println("===XXXXXXXXXX==========");
            Date date= new Date();
            System.out.println(new Timestamp(date.getTime()));
            System.out.println("database name:" + db.getName());
            System.out.println("collection:" + collectionName);
            System.out.println("number of Documents:" + db.getCollection(collectionName).count());


            long lastTotal = 0;
            while (true) {
                JSONObject monitor = new JSONObject();
                TimeUnit.SECONDS.sleep(3 * js.getInt("delay"));

                date= new Date();
                monitor.put("timestamp",new Timestamp(date.getTime()));

                // unique messages
                MongoCursor<String> distMessages= db.getCollection(collectionName).distinct("text", String.class).iterator();

                int uniqueMessages = 0;
                while (distMessages.hasNext()) {
                    uniqueMessages++;
                    distMessages.next();
                }
                monitor.put("messages", uniqueMessages);

                // distinct users
                MongoCursor<String> distUsers= db.getCollection(collectionName).distinct("user", String.class).iterator();

                int uniqueUsers = 0;
                while (distUsers.hasNext()) {
                    uniqueUsers++;
                    distUsers.next();
                }
                monitor.put("users", uniqueUsers);

                // number since last checkpoint
                long collectionSize = db.getCollection(collectionName).count();
                monitor.put("new", collectionSize - lastTotal);
                lastTotal = collectionSize;

                // number messages for each status
                AggregateIterable<Document> statusMessage = db.getCollection(collectionName).aggregate(asList(
                        new Document("$group", new Document("_id", "$status").append("count", new Document("$sum", 1)))));

                JSONArray statuses = new JSONArray();
                statusMessage.forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        statuses.put(document.toJson());
                        System.out.println(document.toJson());
                    }
                });
                monitor.put("statusStats", statuses);

                // number messages for each recepients
                AggregateIterable<Document> destination = db.getCollection(collectionName).aggregate(asList(
                        new Document("$group", new Document("_id", "$recepient").append("count", new Document("$sum", 1)))));

                JSONArray recepients = new JSONArray();
                destination.forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        recepients.put(document.toJson());
                        System.out.println(document.toJson());
                    }
                });
                monitor.put("recepientStats", recepients);
            }

        } catch (FileNotFoundException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }
}
