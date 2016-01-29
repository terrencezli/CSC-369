import com.mongodb.MongoClient;

import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.util.JSON;
import org.bson.Document;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import org.bson.Document;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
        FileWriter fw = null;

        JSONObject checkpoint = new JSONObject();
        JSONArray messageTotals = new JSONArray();
        JSONArray userTotals = new JSONArray();
        JSONArray newMessageTotals = new JSONArray();

        Logger logger = Logger.getLogger("org.mongodb.driver");  // turn off logging
        logger.setLevel(Level.OFF);                              // this lets us squash a lot

        try {
            fw = new FileWriter(new File("server_log.txt"));
            t = new JSONTokener(new FileReader(new File(args[0])));
            js = new JSONObject(t);
            c = new MongoClient(js.getString("mongo"));  // connect to server
            db = c.getDatabase(js.getString("database"));
            checkpoint.put("recordType", "monitor totals");

            String collectionName = js.getString("collection");

            // drops collection
            db.getCollection(js.getString("monitor")).drop();

            s = new Scanner(new File(js.getString("words")));
            list = new ArrayList<String>();
            while (s.hasNext()){
               list.add(s.next());
            }
            s.close();

            String printout = "===XXXXXXXXXX==========\n";
            Date date= new Date();
            printout += new Timestamp(date.getTime()) + "\n";
            printout += "database name:" + db.getName() + "\n";
            printout += "collection:" + collectionName + "\n";
            printout += "number of Documents:" + db.getCollection(collectionName).count() + "\n";
            System.out.println(printout);
            fw.write(printout);
            fw.flush();

            long lastTotal = 0;

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
                
                

                monitor.put("new", collectionSize - lastTotal);
                lastTotal = collectionSize;

                // number messages for each status
                AggregateIterable<Document> statusMessage = db.getCollection(collectionName).aggregate(asList(
                        new Document("$group", new Document("_id", "$status").append("count", new Document("$sum", 1)))));

                final JSONArray statuses = new JSONArray();
                statusMessage.forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        statuses.put(document.toJson());
                        //System.out.println(document.toJson());
                    }
                });
                monitor.put("statusStats", statuses);

                // number messages for each recepients
                AggregateIterable<Document> destination = db.getCollection(collectionName).aggregate(asList(
                        new Document("$group", new Document("_id", "$recepient").append("count", new Document("$sum", 1)))));

                final JSONArray recepients = new JSONArray();
                destination.forEach(new Block<Document>() {
                    @Override
                    public void apply(final Document document) {
                        recepients.put(document.toJson());
                        //System.out.println(document.toJson());
                    }
                });
                monitor.put("recepientStats", recepients);

                System.out.println(monitor.toString(2));
                fw.write(monitor.toString(2));
                fw.flush();

                // checkpoint
                messageTotals.put(uniqueMessages);
                userTotals.put(uniqueUsers);
                newMessageTotals.put(collectionSize - lastTotal);

                if (db.getCollection(js.getString("monitor")).count() > 0) {
                    db.getCollection(js.getString("monitor")).drop();
                }

                Document myDoc = Document.parse(checkpoint.toString());
                db.getCollection(js.getString("monitor")).insertOne(myDoc);
            }

        } catch (InterruptedException | JSONException | IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
