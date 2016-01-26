/**
 * Created by terrence and bryan on 1/25/16.
 */
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;


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

            while (true) {
                // sleep amount
                TimeUnit.SECONDS.sleep(Long.parseLong(js.getString("delay")));

                
            }

        } catch (IOException | JSONException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
