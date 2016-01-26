/**
 * Created by terrence and bryan on 1/25/16.
 */
import java.io.*;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import java.util.concurrent.TimeUnit;




public class thghtShreClient {

    public static void main(String args[]) {
        JSONObject prop = new JSONObject();
        JSONTokener t;
        JSONObject js;

        MongoClient c;
        MongoDatabase db;

        Logger logger = Logger.getLogger("org.mongodb.driver");  // turn off logging
        logger.setLevel(Level.OFF);                              // this lets us squash a lot

        try {
            t = new JSONTokener(new FileReader(new File(args[0])));
            js = new JSONObject(t);

            c = new MongoClient(js.getString("mongo"));  // connect to server
            db = c.getDatabase(js.getString("database"));    // grab database called "alex"

            // server side
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {

                    }
                }
            }).run();

            /*while (true) {
                // sleep amount
                TimeUnit.SECONDS.sleep(Long.parseLong(js.getString("delay")));

                
            }*/

			                                                      // of annoying messages
			String collectionName = js.getString("collection"); 

            //print timestamp
			System.out.println("===XXXXXXXXXX==========");
			java.util.Date date= new java.util.Date();
			System.out.println(new Timestamp(date.getTime()));
			System.out.println("database name:" + db.getName());
			System.out.println("collection:" + collectionName);
			System.out.println("number of Documents:" + db.getCollection(collectionName).count());
			
			 
			MongoCollection<Document> dox = db.getCollection("foo");  // grab collection "foo"
			
			FindIterable<Document> result = dox.find();   // retrieve everything in collection foo
			
			result.forEach(new Block<Document>() {        // print each retrieved document
			     public void apply(final Document d) {
			         System.out.println(d);
			     }
			
			  });		
			
			/*Document o = new Document();    // create a new JSON document object
			o.append("type", "email");      // fill it with stuff
			o.append("content", "lol");
			 
			System.out.println("= = = = = = = = = = = = = = =");
			System.out.println(o);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
			db.getCollection("foo").insertOne(o);   // insert one object into collection foo
            */
			
		    System.out.println("\n\n ===  SUCCESS!! === \n\n");
			
		} catch (Exception e) {
		
		  System.out.println(e);
		
		  System.out.println("=============================");
		
		}
        
        return;
    }
    
    
}
	