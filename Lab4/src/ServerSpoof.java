import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

            // drops collection
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


            long lastTotal = 0;
            long insertedSince = 0;
            
            System.out.println("start loop");
            while (true) {
                JSONObject monitor = new JSONObject();
                
                Date previous = new Date();        
                TimeUnit.SECONDS.sleep(2);

                date= new Date();
                monitor.put("timestamp",new Timestamp(date.getTime()));

                System.out.println("unique messages");
                // unique messages
                MongoCursor<String> distMessages= db.getCollection(collectionName).distinct("text", String.class).iterator();

                int uniqueMessages = 0;
                while (distMessages.hasNext()) {
                    uniqueMessages++;
                    distMessages.next();
                }
                monitor.put("messages", uniqueMessages);

                
                System.out.println("unique users");
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
                
                System.out.println("word Filter");
                //query for text with words in wordFilter
                for(String str: list) {
                	
     
            		db.getCollection(collectionName).createIndex(new Document("text", "text"));
            		
            		System.out.println(new Timestamp(previous.getTime()).toString());
            		
            		Document inner = new Document("$search", str);
            		Document query = new Document("$text", inner);
            		Document inner2 = new Document("$gt", new Timestamp(previous.getTime()).toString());
            		Document query2 = new Document("timestamp", inner2);
            		//.append("timestamp", inner2)
            		Iterator count=db.getCollection(collectionName).find(query.append("timestamp", inner2)).iterator();

                    int wordCount = 0;
                    while(count.hasNext()){
                    	wordCount++;
                    	System.out.println(str + ": " + count.next().toString());
                    } 
                    
                	//System.out.println(str + ": " + wordCount);
                }
                
                
            }

        } catch (FileNotFoundException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }
}
