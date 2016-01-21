import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Histogram {
	long inResponse;
	long notResponse;
	HashMap<Integer, Integer> numWords;
	Status s;
	Recepient r;
	
	public Histogram() {
		s = new Status();
		r = new Recepient();
		inResponse = 0;
		notResponse = 0;
		numWords = new HashMap<Integer, Integer>();
	}
	
	public void calculate(JSONObject j) throws JSONException {
		int st = j.getString("text").split("\\s+").length;
		int count = numWords.containsKey(st) ? numWords.get(st) : 0;
		numWords.put(st, count + 1);
		
		if (j.has("in-response"))
			inResponse++;
		else
			notResponse++;
		
		s.add(j);
		r.add(j);
	}
	
	public String toString() {
		return "Distribution of Messages\n" + s.toString() +  
				"\n\nDistribution of Recepients\n" + r.toString() +
				"\n\nin-response: " + inResponse + 
				"\nnot in-response: " + notResponse + 
				"\n\nDistribution of Words: " + numWords.toString();
	}
	
	public JSONObject toJSON(JSONObject js) throws JSONException {
		js.put("Distribution of Messages",s.toString());  
		js.put("Distribution of Recepients",r.toString());
		js.put("in-response",inResponse); 
		js.put("not in-response",notResponse); 
		js.put("Distribution of Words",numWords.toString());
		
		return js;
	}
}