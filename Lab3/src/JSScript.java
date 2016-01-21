import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Created by terrence on 1/20/16.
 */
public class JSScript {

    public static void main(String args[]) {

        if (args.length == 3) {
            String databaseName = args[1];
            String collectionName = args[2];
            Scanner scan;

            try {
                scan = new Scanner(new File(args[0]));
                String fileName = "";

                if (collectionName.equals("thot")) {
                    fileName = "ThghtShreScript.js";
                }
                else if (collectionName.equals("fudd")) {
                    fileName = "beFuddledScript.js";
                }
                else {
                    System.out.println("Enter a valid collection name.");
                    System.exit(0);
                }

                FileWriter fw = new FileWriter(fileName);
                fw.write("connection = new Mongo();\n");
                fw.write("db = connection.getDB(\"" + databaseName + "\");\n\n");
                fw.write("db." + collectionName + ".insert(\n");

                while (scan.hasNext()) {
                    fw.write(scan.nextLine());
                }

                fw.write(");");
                fw.flush();
                fw.close();



            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        else {
            System.out.println("Paramaters: <file name> <database name> <collection name>");
        }
    }
}
