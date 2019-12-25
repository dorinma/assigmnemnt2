package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */

import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.subscribers.Intelligence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class MI6Runner {


    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader("input.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            //---------Inventory---------
            JSONArray jaInventory = (JSONArray)jsonObject.get("inventory");
            String[]loadInventory = new String[jaInventory.size()];
            for (int i = 0; i < loadInventory.length; i++) {
                loadInventory[i] = (String) jaInventory.get(i);
            }
            Inventory.getInstance().load(loadInventory);

            //---------Squad---------
            JSONArray jaSquad = (JSONArray)jsonObject.get("squad");
            Agent[]agents = new Agent[jaSquad.size()];
            for (int i = 0; i < agents.length; i++) {
                JSONObject jsonObj = (JSONObject) jaSquad.get(i);
                String name = (String)jsonObj.get("name");
                String serNum = (String) jsonObj.get("serialNumber");
                Agent a = new Agent(serNum, name);
                agents[i] = a;
            }
            Squad.getInstance().load(agents);

            //---------Services---------
            JSONArray jaServices = (JSONArray)jsonObject.get("services");

            //---Intelligence---
            JSONArray jaIntelligence = (JSONArray)jsonObject.get("intelligence");
            JSONArray jaMissions = (JSONArray)jsonObject.get("missions");
            //TODO do we need to differ between missions of intelligence a and intelligence b? two tables in json file

            List<MissionInfo> intl = new ArrayList<>();
           // Intelligence[]intelligence = new Intelligence[jaIntelligence.size()];
            for (int i = 0; i < jaMissions.size(); i++) {

                MissionInfo minfo = new MissionInfo();
                minfo.setMissionName((String)jaMissions.get(i));
                //intl.add();
            }
            //Intelligence intl = new Intelligence();
            //intl.setMissions();
            //JSONObject jsonObj = (JSONObject) jaServices.






        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
