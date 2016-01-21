import org.json.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class thghtShreStats {
	
	public static void main (String[] args) throws Exception
	{
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
			
			
			if (args.length > 2) {
				JSONObject js = new JSONObject();
				FileWriter fw = new FileWriter(args[1]);
				b.toJSON(js);
				h.toJSON(js);
				sm.toJSON(js);
				st.toJSON(js);
				re.toJSON(js);
				fw.write(js.toString());
				fw.flush();
				fw.close();
			}
				
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
