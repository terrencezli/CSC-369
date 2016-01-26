/**
 * Created by terrence and bryan on 1/25/16.
 */
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.DBCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;

import java.util.concurrent.TimeUnit;




public class thghtShreClient {

    public static void main(String args[]) {
        JSONObject prop = new JSONObject();
        JSONTokener t;
        JSONObject js;

        MongoClient c;
        MongoDatabase db;
        FileWriter fw = null;
        

        Logger logger = Logger.getLogger("org.mongodb.driver");  // turn off logging
        logger.setLevel(Level.OFF);                              // this lets us squash a lot

        try {
        	
        	fw = new FileWriter(new File("log.txt"));
            t = new JSONTokener(new FileReader(new File(args[0])));
            js = new JSONObject(t);

            c = new MongoClient(js.getString("mongo"));  // connect to server
            db = c.getDatabase(js.getString("database"));    // grab database called "alex"

            // server side
			// server side
			new Thread(new Runnable() {
				MongoClient c;
				MongoDatabase db;
				JSONTokener t;
				JSONObject js;
				Scanner s;
				ArrayList<String> list;

				@Override
				public void run() {
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
			}).start();

            
            
            //print timestamp
            String collectionName = js.getString("collection"); 
            
            
			System.out.println("===XXXXXXXXXX==========");
			Date date= new Date();
			System.out.println(new Timestamp(date.getTime()));
			System.out.println("database name:" + db.getName());
			System.out.println("collection:" + collectionName);
			System.out.println("number of Documents:" + db.getCollection(collectionName).count());
			
			 
            int cycles = 0;
            //Client Code
            while (true) {

                // sleep amount
                TimeUnit.SECONDS.sleep(js.getInt("delay"));
                
                JSONObject gen = thghtShreGen.generate();
                date= new Date();
                gen.put("timestamp",new Timestamp(date.getTime()));
                
                Document myDoc = Document.parse(gen.toString());
                db.getCollection(collectionName).insertOne(myDoc);
                System.out.println(gen.toString(2));
                fw.write(gen.toString(2));
                fw.flush();
                
                if (cycles == 40) {
            		String user = gen.getString("user");
            		int count = 0;
            		Document query = new Document();                // create a query document
            		query.append("user", user);    
            		
            		String printout = "=============================\n";
            		printout += "Total Messages: " +db.getCollection(collectionName).count();
            		printout += "\n";

            		FindIterable<Document> result = db.getCollection(collectionName).find(query);
            		Iterator it = result.iterator();
            		while(it.hasNext()){
            			count++;
            			it.next();
            		}
            		
            		printout += "Messages by " + user + ": " + count;
            		printout += "\n=============================\n";
            		cycles = 0;
            		
            		System.out.println(printout);
            		fw.write(printout);
                    fw.flush();
            		
            	}
                cycles++;
            }
            
		} catch (Exception e) {
		
		  System.out.println(e);
		
		  System.out.println("=============================");
		
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        
        
        return;
    }
    
    
}
	
