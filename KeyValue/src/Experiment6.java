/**
 * Lab 1 Part 1
 * Terrence Li and Bryan Sugiarto
**/

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Experiment6
{
	public static Integer messageCount = 1;

	// "messageId": <messageId>,
	// "user": <userId>,
	// "status": <messageStatus>,
	// "recepient": <recepient>,
	// optional "in-response: <originalMessageId>,"
	// "text": <messageText>
	public static JSONObject buildMessage(String userNum, String status, boolean inResponse, String messageText) throws Exception {
		JSONObject obj = new JSONObject();
		Random rnd = new Random();
		
		obj.put("messageId", messageCount);

		String user = userNum;
		obj.put("user", user);
		obj.put("status", status);
		
		// checks status and applies appropriate recepient
		int rec = rnd.nextInt(10);
		if (status.equals("protected")) {
			String userId = getUser();

			if (rec < 7)
				obj.put("recepient", "subscribers");
			else if (rec == 7 || rec == 8)
				obj.put("recepient", "self");
			else {
				while (userId.equals(userNum)) {
					userId = getUser();
				}

				obj.put("recepient", userId);
			}
		}
		else if (status.equals("private")) {
			String userId = getUser();
			
			if (rec <= 8) {
				while (userId.equals(userNum)) {
					userId = getUser();
				}
				obj.put("recepient", userId);
			}
			else {
				obj.put("recepient", "self");
			}
		}
		else {
			if (rec < 4) {
				obj.put("recepient", "all");
			}
			else if (rec >= 4 && rec < 8) {
				obj.put("recepient", "subscribers");
			}
			else if (rec == 8) {
				obj.put("recepient", "self");
			}
			else {
				String userId = getUser();

				while (userId.equals(userNum)) {
					userId = getUser();
				}

				obj.put("recepient", userId);
			}
		}

		// if in-response provide a messageId
		if (inResponse && messageCount > 1) {
			int response = rnd.nextInt(messageCount);

			while (response == 0)
				response = rnd.nextInt(messageCount);

			obj.put("in-response", response);
		}

		obj.put("text", messageText);

		messageCount++;
		
		return obj;
	}

	public static String getUser() {
		Random rnd = new Random();
		return "u" + rnd.nextInt(10000);
	}
	
	public static String getStatus() {
		Random rnd = new Random();
		int val = rnd.nextInt(10);
		
		if (val < 6) {
			return "public";
		}
		else if (val >= 6 && val <=7) {
			return "protected";
		}
		else {
			return "private";
		}
	}

	public static String randomMessage(ArrayList<String> words) {
		Random rnd = new Random();
		
		// number of words is 2-20 words
		int numWords = rnd.nextInt(19) + 2;
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < numWords; i++) {
			int randWord = rnd.nextInt(words.size());
			
			if (i < numWords-1)
				message.append(words.get(randWord) + " ");
			else
				message.append(words.get(randWord));
		}
		
		return message.toString();
	}
	
	public static double roundUp(int val) {
		return Math.round((val + 5)/ 10.0) * 10.0;
	}
	
	public static double average(List<Long> list) {
		long total = 0;
		
		for (long l : list) {
			total += l;
		}
		
		return (double)total/list.size();
	}
	
	public static double stdDev(List<Long> list) {
		double sqSum = 0;
		double mean = average(list);
		
		for (long l : list) {
			sqSum += Math.pow((l - mean), 2);
		}
		
		return Math.sqrt(sqSum/list.size());
	}
	
	public static void rangeShard(int totalMessages, int totalCollections, KeyValueStore kvs, ArrayList<String> words, int numTrials) throws Exception {
		int lowerBound = 1;
		int upperBound = 100/10;
		int messageCounter = 0;
		String keyName = "";
		Random rnd = new Random();
		
		for (int i = 0; i < 10; i++) {	
			keyName = lowerBound + "-" + upperBound;
			
			kvs.addCollection(keyName);
			
			for (int j = 0; j < 100/10; j++) {
				JSONObject js = buildMessage(getUser(), getStatus(), rnd.nextBoolean(), randomMessage(words));
				messageCounter++;
				kvs.getCollection(keyName).put(j, js);
			}
			
			lowerBound += 100/10;
			upperBound += 100/10;
		}
		
		// leftovers
		int idVal = 100/10;
		while (messageCounter < 100) {
			JSONObject js = buildMessage(getUser(), getStatus(), rnd.nextBoolean(), randomMessage(words));
			messageCounter++;
			kvs.getCollection(keyName).put(idVal++, js);
		}
		
		List<Long> times = new ArrayList<Long>();
		for (int i = 0; i < numTrials; i++) {
			// random messageId
			int chosenObj = rnd.nextInt(100);
			// parse kvs boundaries
			int findL = (int)roundUp(chosenObj) - 9;
			int findU = (int)roundUp(chosenObj);
			String kvC = findL + "-" + findU;
			
			// time
			long start = System.nanoTime();
			
			KVCollection kvCollect = kvs.getCollection(kvC);
			JSONObject found = kvCollect.get(chosenObj%(100/10));
			
			long finish = System.nanoTime();
			
			times.add(finish-start);
		}
		
		System.out.println("Average for range keys for " + numTrials + " trials: " + average(times) + " nanoseconds");
		System.out.println("Standard Deviation " + stdDev(times));
	}
	
	public static void roundRobinShard(int totalMessages, int totalCollections, KeyValueStore kvs, ArrayList<String> words, int numTrials) throws Exception {
		String keyName = "";
		Random rnd = new Random();
		
		for (int i = 0; i < 10; i++) {	
			keyName = i + "";
			
			kvs.addCollection(keyName);
		}
		
		for (int j = 0; j < 100; j++) {
			JSONObject js = buildMessage(getUser(), getStatus(), rnd.nextBoolean(), randomMessage(words));
			kvs.getCollection(rnd.nextInt(10) + "").put((Integer)js.get("messageId"), js);
		}
		
		List<Long> times = new ArrayList<Long>();
		for (int i = 0; i < 100; i++) {
			// random messageId
			int chosenObj = rnd.nextInt(100)+1;
			
			// time
			long start = System.nanoTime();
			long finish = 0L;
			
			// parse kvs boundaries
			for (int j = 0; j < 10; j++) {
				KVCollection kvCollect = kvs.getCollection(j+"");
				//System.out.println(kvCollect.toString());
				
				if (kvCollect.containsKey(chosenObj)) {	
					JSONObject found = kvCollect.get(chosenObj);
					finish = System.nanoTime();
				}
			}
				
			times.add(finish-start);
		}
		
		System.out.println("Average for round robin for " + numTrials + " trials: " + average(times) + " nanoseconds");
		System.out.println("Standard Deviation " + stdDev(times));
		System.out.println();
	}
	
	public static void main (String[] args) throws Exception
	{
		ArrayList<String> words = new ArrayList<String>();
		KeyValueStore kvs = new KeyValueStore();
		
		Scanner scan = new Scanner(System.in);
		Random rnd = new Random();
		
		//System.out.println("Enter file name: ");
		Scanner file;
		if (args.length < 3)
			file = new Scanner(new FileReader("sense.txt"));
		else 
			file = new Scanner(new FileReader(args[2]));
		
		while(file.hasNextLine()) {
			words.add(file.nextLine());
		}
		
		rangeShard(100, 10, kvs, words, 100);
		kvs.clear();
		messageCount = 1;
		roundRobinShard(100, 10, kvs, words, 100);
		
		rangeShard(100, 10, kvs, words, 1000);
		kvs.clear();
		messageCount = 1;
		roundRobinShard(100, 10, kvs, words, 1000);
		
		rangeShard(100, 10, kvs, words, 10000);
		kvs.clear();
		messageCount = 1;
		roundRobinShard(100, 10, kvs, words, 10000);
		
		rangeShard(100, 10, kvs, words, 100000);
		kvs.clear();
		messageCount = 1;
		roundRobinShard(100, 10, kvs, words, 100000);
		
		//kvs.print();
	}
}