package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
//import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileReader;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    public static String fileName;
  //  public static JSONParser parser;

    public static void main(String[] args) {
        System.out.println("gdcygff");

        /*
            parser = new JSONParser();
            fileName = "C:/Users/user/Desktop/Semester_C/SPL/Assignement2/MI6/src/main/java/bgu/spl/mics/application/input.json";

            Inventory myInventory = Inventory.getInstance();
            List<String> gadgets = new ArrayList<>();

            try {
                Object obj = parser.parse(new FileReader(fileName));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray jsonInventory = (JSONArray) jsonObject.get("inventory");

                Iterator<String> iterator = jsonInventory.iterator();
                String gdg;
                while (iterator.hasNext()) {
                    gdg = iterator.next();
                    System.out.println(gdg);
                    gadgets.add(gdg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


*/



    }
}
