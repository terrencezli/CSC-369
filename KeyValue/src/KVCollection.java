import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.*;

public class KVCollection {
	Map<Integer, JSONObject> kv = new HashMap<Integer, JSONObject>();
	
	public KVCollection() {
	}
	
	public boolean clear() {
		kv.clear();
		
		return kv.isEmpty();
	}
	
	public boolean containsKey(int key) {
		return kv.containsKey(key);
	}
	
	public JSONObject get(int key) {
		return kv.get(key);
	}
	
	public int put(int key, JSONObject value) {
		if (kv.containsKey(key)) {
			return -1;
		}
		else {
			try {
				kv.put(key, value);
			}
			catch(Throwable t) {
				return 0;
			}
			
			return 1;
		}
	}
	
	public int remove(int key) {
		if (!kv.containsKey(key)) {
			return -1;
		}
		else {
			try {
				kv.remove(key);
			}
			catch(Throwable t) {
				return 0;
			}
			
			return 1;
		}
	}
	
	public int replace(int key, JSONObject value) {
		if (!kv.containsKey(key)) {
			return -1;
		}
		else {
			try {
				kv.put(key, value);
			}
			catch(Throwable t) {
				return 0;
			}
			
			return 1;
		}
	}
	
	public int size() {
		return kv.size();
	}
	
	public int upsert(int key, JSONObject value) {
		if (!kv.containsKey(key)) {
			kv.put(key, value);
			return 1;
		}
		else {
			try {
				kv.put(key, value);
			}
			catch(Throwable t) {
				return 0;
			}
			
			return 2;
		}
	}
	
	public JSONArray find(String jsonFieldName, Object jsonFieldValue) {
		Iterator it = kv.entrySet().iterator();
		JSONArray ret = new JSONArray();
		
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        int key = (Integer)pair.getKey();
	        JSONObject value = (JSONObject)pair.getValue();
	        
	        try {
		        if (value.getString(jsonFieldName).equals(jsonFieldValue)) {
		        	JSONObject j = new JSONObject();
		        	j.put("key", key);
		        	j.put("value", value);
		        	ret.put(j);
		        }
	        } catch(Exception e) {
	        	System.out.println("JSON error");
	        	System.exit(0);
	        }
	        
	        
	        it.remove();
	    }
	    
	    return ret;
	}
	
	public String toString() {
		return kv.toString();
	}
}
