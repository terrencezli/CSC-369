import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubsetMessages {
	Status s;
	Recepient r;
	
	long totMessages;
	long inResponseWords;
	long sqIRW;
	long inResponseChar;
	long sqIRC;
	
	long notResponseWords;
	long sqNRW;
	long notResponseChar;
	long sqNRC;
	
	public SubsetMessages() {
		s = new Status();
		r = new Recepient();
		
		totMessages = 0;
		inResponseWords = 0;
		inResponseChar = 0;
		
		notResponseWords = 0;
		notResponseChar = 0;
	}
	
	void calculate(JSONObject j) throws JSONException {
		s.add(j);
		r.add(j);
		totMessages++;
		
		if (j.has("in-response")) {
			inResponseWords += j.getString("text").split("\\s+").length;
			sqIRW += Math.pow(j.getString("text").split("\\s+").length, 2);
			inResponseChar += j.getString("text").length();
			sqIRC += Math.pow(j.getString("text").length(), 2);
		}
		else {
			notResponseWords += j.getString("text").split("\\s+").length;
			sqNRW += Math.pow(j.getString("text").split("\\s+").length, 2);
			notResponseChar += j.getString("text").length();
			sqNRC += Math.pow(j.getString("text").length(), 2);
		}	
	}
	
	double getStdDev(long sqSum, long totalMessages, double mean) {
		return Math.sqrt((double)sqSum / totalMessages - Math.pow(mean, 2));
	}
	
	public String toString() {
		
		return "Average Status Words" + 
				"\npublic: " + (double)s.totPublicWords/s.totPublic +
				"\npublicW std: " + getStdDev(s.sqPubW, s.totPublic, (double)s.totPublicWords/s.totPublic) +
				"\nprotected: " + (double)s.totProtectedWords/s.totProtected + 
				"\nprotectedW std: " + getStdDev(s.sqProtectedW, s.totProtected, (double)s.totProtectedWords/s.totProtected) +
				"\nprivate: " + (double)s.totPrivateWords/s.totPrivate +
				"\nprivateW std: " + getStdDev(s.sqPrivateW, s.totPrivate, (double)s.totPrivateWords/s.totPrivate) +
				
				"\n\nAverage Status Char" + 
				"\npublic: " + (double)s.totPublicChar/s.totPublic +
				"\npublicC std: " + getStdDev(s.sqPubC, s.totPublic, (double)s.totPublicChar/s.totPublic) +
				"\nprotected: " + (double)s.totProtectedChar/s.totProtected + 
				"\nprotectedC std: " + getStdDev(s.sqProtectedC, s.totProtected, (double)s.totProtectedChar/s.totProtected) +
				"\nprivate: " + (double)s.totPrivateChar/s.totPrivate +
				"\nprivateC std: " + getStdDev(s.sqPrivateC, s.totPrivate, (double)s.totPrivateChar/s.totPrivate) +
				
				"\n\nAverage Recepient Words" + 
				"\nall: " + (double)r.allWords/r.all +
				"\nStdev allW: " + getStdDev(r.sqAllW, r.all, (double)r.allWords/r.all) +
				"\nself: " + (double)r.selfWords/r.self + 
				"\nStdev selfW: " + getStdDev(r.sqSelfW, r.self, (double)r.selfWords/r.all) +
				"\nsubscribers: " + (double)r.subscribersWords/r.subscribers +
				"\nStdev subscribersW: " + getStdDev(r.sqSubscribersW, r.subscribers, (double)r.subscribersWords/r.subscribers) +
				"\nuserId: " + (double)r.userIdWords/r.userId +
				"\nStdev userIdW: " + getStdDev(r.sqUserIdW, r.userId, (double)r.userIdWords/r.userId) +
				
				"\n\nAverage Recepient Char" + 
				"\nall: " + (double)r.allChar/r.all +
				"\nStdev allC: " + getStdDev(r.sqAllC, r.all, (double)r.allChar/r.all) +
				"\nself: " + (double)r.selfChar/r.self + 
				"\nStdev selfC: " + getStdDev(r.sqSelfC, r.self, (double)r.selfChar/r.self) +
				"\nsubscribers: " + (double)r.subscribersChar/r.subscribers +
				"\nStdev subscribersC: " + getStdDev(r.sqSubscribersC, r.subscribers, (double)r.subscribersChar/r.subscribers) +
				"\nuserId: " + (double)r.userIdChar/r.userId +
				"\nStdev userIdC: " + getStdDev(r.sqUserIdC, r.userId, (double)r.userIdChar/r.userId) +
				
				"\n\nAvg In-response Words: " + (double)inResponseWords/totMessages +
				"\nStandard Deviation IRW: " + getStdDev(sqIRW, totMessages, (double)inResponseWords/totMessages) + 
				"\nAvg In-response Chars: " + (double)inResponseChar/totMessages +
				"\nStandard Deviation IRC: " + getStdDev(sqIRC, totMessages, (double)inResponseChar/totMessages) + 
				
				"\n\nAvg Not in-response Words: " + (double)notResponseWords/totMessages +
				"\nStandard Deviation NRW: " + getStdDev(sqNRW, totMessages, (double)notResponseWords/totMessages) +
				"\nAvg Not in-response Chars: " + (double)notResponseChar/totMessages +
				"\nStandard Deviation NRC: " + getStdDev(sqNRC, totMessages, (double)inResponseChar/totMessages);
				
	}
	
	public JSONObject toJSON(JSONObject js) throws JSONException {
		js.put("Average Status Words public",(double)s.totPublicWords/s.totPublic);
		js.put("publicW std",getStdDev(s.sqPubW, s.totPublic, (double)s.totPublicWords/s.totPublic));
		js.put("Average Status Words protected",(double)s.totProtectedWords/s.totProtected);
		js.put("protectedW std",getStdDev(s.sqProtectedW, s.totProtected, (double)s.totProtectedWords/s.totProtected));
		js.put("Average Status Words private",(double)s.totPrivateWords/s.totPrivate);
		js.put("privateW std",getStdDev(s.sqPrivateW, s.totPrivate, (double)s.totPrivateWords/s.totPrivate));
		
		js.put("Average Status Char public",(double)s.totPublicChar/s.totPublic);
		js.put("publicC std",getStdDev(s.sqPubC, s.totPublic, (double)s.totPublicChar/s.totPublic));
		js.put("Average Status Char protected",(double)s.totProtectedChar/s.totProtected);
		js.put("protectedC std",getStdDev(s.sqProtectedC, s.totProtected, (double)s.totProtectedChar/s.totProtected));
		js.put("Average Status Char private",(double)s.totPrivateChar/s.totPrivate);
		js.put("privateC std",getStdDev(s.sqPrivateC, s.totPrivate, (double)s.totPrivateChar/s.totPrivate));
		
		js.put("Average Recepient Words all",(double)r.allWords/r.all);
		js.put("Stdev allW",getStdDev(r.sqAllW, r.all, (double)r.allWords/r.all));
		js.put("Average Recepient Words self",(double)r.selfWords/r.self);
		js.put("Stdev selfW",getStdDev(r.sqSelfW, r.self, (double)r.selfWords/r.all));
		js.put("Average Recepient Words subscribers",(double)r.subscribersWords/r.subscribers);
		js.put("Stdev subscribersW",getStdDev(r.sqSubscribersW, r.subscribers, (double)r.subscribersWords/r.subscribers));
		js.put("Average Recepient Words userId",(double)r.userIdWords/r.userId);
		js.put("Stdev userIdW",getStdDev(r.sqUserIdW, r.userId, (double)r.userIdWords/r.userId));
		
		js.put("Average Recepient Char all",(double)r.allChar/r.all);
		js.put("Stdev allC",getStdDev(r.sqAllC, r.all, (double)r.allChar/r.all));
		js.put("Average Recepient Char self",(double)r.selfChar/r.self);
		js.put("Stdev selfC",getStdDev(r.sqSelfC, r.self, (double)r.selfChar/r.self));
		js.put("Average Recepient Char subscribers",(double)r.subscribersChar/r.subscribers);
		js.put("Stdev subscribersC",getStdDev(r.sqSubscribersC, r.subscribers, (double)r.subscribersChar/r.subscribers));
		js.put("Average Recepient Char userId",(double)r.userIdChar/r.userId);
		js.put("Stdev userIdC",getStdDev(r.sqUserIdC, r.userId, (double)r.userIdChar/r.userId));
		
		js.put("Avg In-response Words",(double)inResponseWords/totMessages);
		js.put("Standard Deviation IRW",getStdDev(sqIRW, totMessages, (double)inResponseWords/totMessages));
		js.put("Avg In-response Chars",(double)inResponseChar/totMessages);
		js.put("Standard Deviation IRC",getStdDev(sqIRC, totMessages, (double)inResponseChar/totMessages));
		
		js.put("Avg Not in-response Words",(double)notResponseWords/totMessages);
		js.put("Standard Deviation NRW",getStdDev(sqNRW, totMessages, (double)notResponseWords/totMessages));
		js.put("Avg Not in-response Chars",(double)notResponseChar/totMessages);
		js.put("Standard Deviation NRC",getStdDev(sqNRC, totMessages, (double)inResponseChar/totMessages));
		
		return js;
	}
}
