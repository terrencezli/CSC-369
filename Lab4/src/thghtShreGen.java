/**
 * Lab 1 Part 1
 * Terrence Li and Bryan Sugiarto
**/

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class thghtShreGen
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
			
			message.append(" " + words.get(randWord));
		}
		
		return message.toString();
	}
	
	public static JSONObject generate() throws Exception {
		ArrayList<String> words = new ArrayList<String>();
		
		Random rnd = new Random();
		
		//System.out.println("Enter file name: ");
		Scanner file;
	
		file = new Scanner(new FileReader("sense.txt"));
		
		while(file.hasNextLine()) {
			words.add(file.nextLine());
		}
		
		JSONObject j = buildMessage(getUser(), getStatus(), rnd.nextBoolean(), randomMessage(words));
			
		//System.out.println(arr.toString(4));
		
		return j;
	}
	
	public static void main (String[] args) throws Exception
	{
		ArrayList<String> words = new ArrayList<String>();
		
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
		
		//System.out.println("Enter number of JSON object: ");
		Integer totalMessages = Integer.parseInt(args[1]);
		
		FileWriter fw = new FileWriter(args[0]);
		fw.write("[");
		fw.write("\n");
		
		for (int i = 0; i < totalMessages; i++) {
			JSONObject j = buildMessage(getUser(), getStatus(), rnd.nextBoolean(), randomMessage(words));
			fw.write(j.toString(2));

			if (i < totalMessages - 1)
				fw.write(",");

			fw.write("\n");
		}

		fw.write("]");
		fw.write("\n");
		fw.flush();
		fw.close();
	}
}