/**
 * Created by terrence on 1/25/16.
 */
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class thghtShreClient {

    public static void main(String args[]) {
        JSONObject prop = new JSONObject();

        try {
            JSONTokener t = new JSONTokener(new FileReader(new File("configuration.properties")));
             
            JSONObject js = new JSONObject(t);
            System.out.println(js.getString("mongo"));


        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
}
