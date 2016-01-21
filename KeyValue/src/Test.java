import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Test {

	public static void create(KVCollection kv) throws JSONException {
		JSONObject j = new JSONObject();
		j.put("name", "bob");
		j.put("age", "7");
		
		JSONObject j1 = new JSONObject();
		j1.put("name", "berry");
		j1.put("age", "6");
		
		JSONObject j2 = new JSONObject();
		j2.put("name", "bob");
		j2.put("age", "5");
		
		JSONObject j3 = new JSONObject();
		j3.put("name", "bob");
		j3.put("age", "4");
		
		kv.upsert(1, j);
		kv.upsert(2, j1);
		kv.upsert(3, j2);
		kv.upsert(4, j3);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		KVCollection kv = new KVCollection();
		KVCollection kv1 = new KVCollection();
		
		create(kv);
		create(kv);
		
		KeyValueStore ks = new KeyValueStore();
		ks.addCollection("hey");
		ks.addCollection("bye");
		
		JSONArray js = kv.find("name", "bob");
		System.out.println(js.toString(2));
		
	}

}
