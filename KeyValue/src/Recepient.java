import org.json.JSONException;
import org.json.JSONObject;

public class Recepient {
	long all;
	long allWords;
	long sqAllW;
	long allChar;
	long sqAllC;
	
	long self;
	long selfWords;
	long sqSelfW;
	long selfChar;
	long sqSelfC;
	
	long subscribers;
	long subscribersWords;
	long sqSubscribersW;
	long subscribersChar;
	long sqSubscribersC;
	
	long userId;
	long userIdWords;
	long sqUserIdW;
	long userIdChar;
	long sqUserIdC;
	
	long allIR;
	long allNR;
	
	long selfIR;
	long selfNR;
	
	long subscriberIR;
	long subscriberNR;
	
	long userIdIR;
	long userIdNR;

	public Recepient() {
		all = 0;
		allWords = 0;
		allChar = 0;
		
		self = 0;
		selfWords = 0;
		selfChar = 0;
		
		subscribers = 0;
		subscribersWords = 0;
		subscribersChar = 0;
		
		userId = 0;
		userIdWords = 0;
		userIdChar = 0;
		
		allIR = 0;
		allNR = 0;
		
		selfIR = 0;
		selfNR = 0;
		
		subscriberIR = 0;
		subscriberNR = 0;
		
		userIdIR = 0;
		userIdNR = 0;
	}
	
	public void add(JSONObject j) throws JSONException {
		String s = j.getString("recepient");
		if (s.equals("all")) {
			all++;
			allWords += j.getString("text").split("\\s+").length;
			allChar += j.getString("text").length();
			
			sqAllW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqAllC += Math.pow(j.getString("text").length(), 2);
			
			if (j.has("in-response"))
				allIR++;
			else
				allNR++;
		}
		else if(s.equals("self")) {
			self++;
			selfWords += j.getString("text").split("\\s+").length;
			selfChar += j.getString("text").length();
			
			sqSelfW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqSelfC += Math.pow(j.getString("text").length(), 2);
			
			if (j.has("in-response"))
				selfIR++;
			else
				selfNR++;
		}
		else if(s.equals("subscribers")) {
			subscribers++;
			subscribersWords += j.getString("text").split("\\s+").length;
			subscribersChar += j.getString("text").length();
			
			sqSubscribersW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqSubscribersC += Math.pow(j.getString("text").length(), 2);
			
			if (j.has("in-response"))
				subscriberIR++;
			else
				subscriberNR++;
		}
		else {
			userId++;
			userIdWords += j.getString("text").split("\\s+").length;
			userIdChar += j.getString("text").length();
			
			sqUserIdW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqUserIdC += Math.pow(j.getString("text").length(), 2);
			
			if (j.has("in-response"))
				userIdIR++;
			else
				userIdNR++;
		}
	}
	
	public String toString() {
		return "all: " + all + "\nself: " + self + "\nsubscribers: " + 
				subscribers + "\nuserId: " + userId;
	}
	
	public String getCH() {
		return "Conditional Histogram In-Response" +
				"\nall in-response: " + allIR +
				"\nall no in-response: " + allNR +
				"\nself in-response: " + selfIR +
				"\nself no in-response: " + selfNR +
				"\nsubscriber in-response: " + subscriberIR +
				"\nsubscriber no in-response: " + subscriberNR +
				"\nuserId in-response: " + userIdIR +
				"\nuserId no in-response: " + userIdNR;
	}
	
	public JSONObject toJSON(JSONObject js) throws JSONException {
		js.put("Conditional Histogram all in-response",allIR);
		js.put("Conditional Histogram all no in-response",allNR);
		js.put("Conditional Histogram self in-response",selfIR);
		js.put("Conditional Histogram self no in-response",selfNR);
		js.put("Conditional Histogram subscriber in-response",subscriberIR);
		js.put("Conditional Histogram subscriber no in-response",subscriberNR);
		js.put("Conditional Histogram userId in-response",userIdIR);
		js.put("Conditional Histogram userId no in-response",userIdNR);
		
		return js;
	}
}