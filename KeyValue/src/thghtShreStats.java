import org.json.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class thghtShreStats {
	
	public static void main (String[] args) throws Exception
	{
		if (args.length <= 2 && args.length > 0) {
			try{
				// JSONTokener is the org.json wrapper around any Reader object 
				JSONTokener t = new JSONTokener(new FileReader(new File(args[0])));
				JSONArray a = new JSONArray(t);
				BasicStats b = new BasicStats();
				Histogram h = new Histogram();
				SubsetMessages sm = new SubsetMessages();
				Status st = new Status();
				Recepient re = new Recepient();
			
				for (int i = 0; i < a.length(); i++) {
					JSONObject j = a.getJSONObject(i);
					
					if (!j.has("recepient") || !j.has("messageId") || !j.has("text")
							|| !j.has("user") || !j.has("status")) {
						System.out.println("BAD FILE");
						System.exit(0);
					}
					
					b.calculate(j);
					h.calculate(j);
					sm.calculate(j);
					
					st.add(j);
					re.add(j);
				}
				
				System.out.println(b.toString());
				System.out.println();
				System.out.println(h.toString());
				System.out.println();
				System.out.println(sm.toString());
				System.out.println();
				System.out.println(st.getCH());
				System.out.println();
				System.out.println(re.getCH());
				
				
				if (args.length >= 2) {
					JSONObject js = new JSONObject();
					FileWriter fw = new FileWriter(args[1]);
					b.toJSON(js);
					h.toJSON(js);
					sm.toJSON(js);
					st.toJSON(js);
					re.toJSON(js);
					fw.write(js.toString(2));
					fw.flush();
					fw.close();
				}
					
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		else if (args.length == 0 || args.length > 2) {
			System.out.println("To run\n" +
					"- java -cp '.:org.json-20120521.jar' thghtShreStats <read-file> <optional-output-file>\n" +
					"- if no optional file, will use only print to console");
			System.exit(0);
		}
	}
}
