import org.json.JSONException;
import org.json.JSONObject;

class Status {
	long totPublic;
	long totPublicWords;
	long sqPubW;
	long totPublicChar;
	long sqPubC;
	
	long totProtected;
	long totProtectedWords;
	long sqProtectedW;
	long totProtectedChar;
	long sqProtectedC;
	
	long totPrivate;
	long totPrivateWords;
	long sqPrivateW;
	long totPrivateChar;
	long sqPrivateC;
	
	Recepient publicRe;
	Recepient protectedRe;
	Recepient privateRe;
	
	Recepient publicIR;
	Recepient protectedIR;
	Recepient privateIR;
	
	Recepient publicNR;
	Recepient protectedNR;
	Recepient privateNR;
	
	public Status() {
		totPublic = 0;
		totPublicWords = 0;
		totPublicChar = 0;
		
		totProtected = 0;
		totProtectedWords = 0;
		totProtectedChar = 0;
		
		totPrivate = 0;
		totPrivateWords = 0;
		totPrivateChar = 0;
		
		publicRe = new Recepient();
		protectedRe = new Recepient();
		privateRe = new Recepient();
		
		publicIR = new Recepient();
		protectedIR = new Recepient();
		privateIR = new Recepient();
		
		publicNR = new Recepient();
		protectedNR = new Recepient();
		privateNR = new Recepient();
	}
	
	public void add(JSONObject j) throws JSONException {
		String s = j.getString("status");
		if (s.equals("public")) {
			totPublic++;
			totPublicWords += j.getString("text").split("\\s+").length;
			totPublicChar += j.getString("text").length();
			sqPubW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqPubC += Math.pow(j.getString("text").length(), 2);
			publicRe.add(j);
			
			if (j.has("in-response"))
				publicIR.add(j);
			else
				publicNR.add(j);
		}
		else if(s.equals("protected")) {
			totProtected++;
			totProtectedWords += j.getString("text").split("\\s+").length;
			totProtectedChar += j.getString("text").length();
			sqProtectedW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqProtectedC += Math.pow(j.getString("text").length(), 2);
			protectedRe.add(j);
			
			if (j.has("in-response"))
				protectedIR.add(j);
			else
				protectedNR.add(j);
		}
		else {
			totPrivate++;
			totPrivateWords += j.getString("text").split("\\s+").length;
			totPrivateChar += j.getString("text").length();
			sqPrivateW += Math.pow(j.getString("text").split("\\s+").length, 2);
			sqPrivateC += Math.pow(j.getString("text").length(), 2);
			privateRe.add(j);
			
			if (j.has("in-response"))
				privateIR.add(j);
			else
				privateNR.add(j);
		}
		
	}
	
	public String toString() {
		return "public: " + totPublic + "\nprotected: " + totProtected + "\nprivate: " + totPrivate;
	}
	
	public String getCH() {
		return "Conditional Histogram Status" + 
				"\npublic" + 
				"\nall: " + publicRe.all +
				"\nself: " + publicRe.self +
				"\nsubscribers: " + publicRe.subscribers +
				"\nuserId: " + publicRe.userId +
				
				"\n\nprotected" +
				"\nall: " + protectedRe.all +
				"\nself: " + protectedRe.self +
				"\nsubscribers: " + protectedRe.subscribers +
				"\nuserId: " + protectedRe.userId +
				
				"\n\nprivate" +
				"\nall: " + privateRe.all +
				"\nself: " + privateRe.self +
				"\nsubscribers: " + privateRe.subscribers +
				"\nuserId: " + privateRe.userId +
				 
				"\n\nin-response" +
				"\npublic" + 
				"\nall: " + publicIR.all +
				"\nself: " + publicIR.self +
				"\nsubscribers: " + publicIR.subscribers +
				"\nuserId: " + publicIR.userId +
				
				"\n\nprotected" +
				"\nall: " + protectedIR.all +
				"\nself: " + protectedIR.self +
				"\nsubscribers: " + protectedIR.subscribers +
				"\nuserId: " + protectedIR.userId +
				
				"\n\nprivate" +
				"\nall: " + privateIR.all +
				"\nself: " + privateIR.self +
				"\nsubscribers: " + privateIR.subscribers +
				"\nuserId: " + privateIR.userId +
				
				"\n\nno in-response" +
				"\npublic" + 
				"\nall: " + publicNR.all +
				"\nself: " + publicNR.self +
				"\nsubscribers: " + publicNR.subscribers +
				"\nuserId: " + publicNR.userId +
				
				"\n\nprotected" +
				"\nall: " + protectedNR.all +
				"\nself: " + protectedNR.self +
				"\nsubscribers: " + protectedNR.subscribers +
				"\nuserId: " + protectedNR.userId +
				
				"\n\nprivate" +
				"\nall: " + privateNR.all +
				"\nself: " + privateNR.self +
				"\nsubscribers: " + privateNR.subscribers +
				"\nuserId: " + privateNR.userId;
		
	}
	
	public JSONObject toJSON(JSONObject js) throws JSONException {
		js.put("Conditional Histogram Status public all",publicRe.all);
		js.put("Conditional Histogram Status public self",publicRe.self);
		js.put("Conditional Histogram Status public subscribers",publicRe.subscribers);
		js.put("Conditional Histogram Status public userId",publicRe.userId);
		
		js.put("Conditional Histogram Status protected all",protectedRe.all);
		js.put("Conditional Histogram Status protected self",protectedRe.self);
		js.put("Conditional Histogram Status protected subscribers",protectedRe.subscribers);
		js.put("Conditional Histogram Status protected userId",protectedRe.userId);
		
		js.put("Conditional Histogram Status private all",privateRe.all);
		js.put("Conditional Histogram Status private self",privateRe.self);
		js.put("Conditional Histogram Status private subscribers",privateRe.subscribers);
		js.put("Conditional Histogram Status private userId",privateRe.userId);
		 
		js.put("Conditional Histogram Status public IR all",publicIR.all);
		js.put("Conditional Histogram Status public IR self",publicIR.self);
		js.put("Conditional Histogram Status public IR subscribers",publicIR.subscribers);
		js.put("Conditional Histogram Status public IR userId",publicIR.userId);
		
		js.put("Conditional Histogram Status protected IR all",protectedIR.all);
		js.put("Conditional Histogram Status protected IR self",protectedIR.self);
		js.put("Conditional Histogram Status protected IR subscribers",protectedIR.subscribers);
		js.put("Conditional Histogram Status protected IR userId",protectedIR.userId);
		
		js.put("Conditional Histogram Status private IR all",privateIR.all);
		js.put("Conditional Histogram Status private IR self",privateIR.self);
		js.put("Conditional Histogram Status private IR subscribers",privateIR.subscribers);
		js.put("Conditional Histogram Status private IR userId",privateIR.userId);
		
		js.put("Conditional Histogram Status public NR all",publicNR.all);
		js.put("Conditional Histogram Status public NR self",publicNR.self);
		js.put("Conditional Histogram Status public NR subscribers",publicNR.subscribers);
		js.put("Conditional Histogram Status public NR userId",publicNR.userId);
		
		js.put("Conditional Histogram Status protected NR all",protectedNR.all);
		js.put("Conditional Histogram Status protected self",protectedNR.self);
		js.put("Conditional Histogram Status protected subscribers",protectedNR.subscribers);
		js.put("Conditional Histogram Status protected userId",protectedNR.userId);
		
		js.put("Conditional Histogram Status private NR all",privateNR.all);
		js.put("Conditional Histogram Status private NR self",privateNR.self);
		js.put("Conditional Histogram Status private NR subscribers",privateNR.subscribers);
		js.put("Conditional Histogram Status private NR userId",privateNR.userId);
		
		return js;
	}
}