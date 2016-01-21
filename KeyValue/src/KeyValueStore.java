import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;


public class KeyValueStore {
	Map<String, KVCollection> store = new HashMap<String, KVCollection>();;
	int MAX_SIZE = Integer.MAX_VALUE;
	
	public KeyValueStore() {
	}
	
	public KeyValueStore(int limit) {
		MAX_SIZE = limit;
	}
	
	public KeyValueStore(Set<String> collections) {
		for (String s : collections) {
			store.put(s, new KVCollection());
		}
	}
	
	public void clear() {
		store.clear();
	}
	
	public int addCollection(String name) {
		if (store.containsKey(name)) {
			return -1;
		}
		else {
			try {
				if (store.size() >= MAX_SIZE) {
					return -2;
				}
					
				store.put(name, new KVCollection());
			}
			catch(Throwable t) {
				return 0;
			}
			
			return 1;
		}
	}
	
	public KVCollection getCollection(String name) {
		return store.get(name);
	}
	
	public Set<String> list() {
		return store.keySet();
	}
	
	public boolean isEmpty() {
		return store.isEmpty();
	}
	
	public int size() {
		return store.size();
	}
	
	public int getNumObjects() {
		Iterator it = store.entrySet().iterator();
		int numObjects = 0;
		
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			KVCollection kv = (KVCollection)pair.getValue();
			numObjects += kv.size();
			it.remove();
		}
		
		return numObjects;
	}
	
	public int getLimit() {
		if (MAX_SIZE == Integer.MAX_VALUE)
			return -1;
		else
			return MAX_SIZE;
	}
	
	public void print() {
		Iterator it = store.entrySet().iterator();
		int numObjects = 0;
		
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			KVCollection kv = (KVCollection)pair.getValue();
			System.out.println(kv.toString());
			numObjects += kv.size();
			it.remove();
		}
	}
}
