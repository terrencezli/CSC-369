import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

 class BasicStats {
	long totMessages;
	HashSet uniqueMessages;
	long totChar;
	long sqCharSum;
	long sqWordSum;
	long totWords;
	double stdDevChar;
	double stdDevWords;
	
	BasicStats() {
		totMessages = 0;
		uniqueMessages = new HashSet();
		totChar = 0;
		sqCharSum = 0;
		totWords = 0;
		sqWordSum = 0;
		stdDevChar = 0;
		stdDevWords = 0;
	}
	
	void calculate(JSONObject j) throws JSONException {
		totMessages++;
		totWords += j.getString("text").split("\\s+").length;
		totChar += j.getString("text").length();
		sqCharSum += Math.pow(j.getString("text").length(), 2);
		sqWordSum += Math.pow(j.getString("text").split("\\s+").length, 2);
		uniqueMessages.add(j.getString("user"));
	}
	
	public String toString() {
		double meanW = (double)totWords/totMessages;
		double stdDevW = Math.sqrt(sqWordSum / totMessages - Math.pow(meanW, 2));
		double meanC = (double)totChar/totMessages;
		double stdDevC = Math.sqrt(sqCharSum/ totMessages - Math.pow(meanC, 2));
		
		return "Basic Stats"
				+ "\nTotal Messages: " + totMessages +
				"\nUnique Messages: " + uniqueMessages.size() +
				"\nAverage Words: " + meanW +
				"\nStandard Deviation Words: " + stdDevW +
				"\nAverage Char: " + meanC +
				"\nStandard Deviation Char: " + stdDevC;
	}
	
	public JSONObject toJSON(JSONObject js) throws JSONException {
		double meanW = (double)totWords/totMessages;
		double stdDevW = Math.sqrt(sqWordSum / totMessages - Math.pow(meanW, 2));
		double meanC = (double)totChar/totMessages;
		double stdDevC = Math.sqrt(sqCharSum/ totMessages - Math.pow(meanC, 2));
		
		js.put("Total Messages", totMessages);
		js.put("Unique Messages",uniqueMessages.size());
		js.put("Average Words",meanW);
		js.put("Standard Deviation Words",stdDevW);
		js.put("Average Char",meanC);
		js.put("Standard Deviation Char",stdDevC);
		
		return js;
	}

}