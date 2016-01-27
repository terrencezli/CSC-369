/**
 * Created by terrence and bryan on 1/25/16.
 */
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;

import java.util.concurrent.TimeUnit;




public class thghtShreClient {

    public static void main(String args[]) {
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
    }
    
    
}
	
