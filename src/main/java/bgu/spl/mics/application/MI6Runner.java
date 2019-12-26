package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
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
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
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
            FileReader reader = new FileReader("C:/Users/hadas zeira/Desktop/Software Engineering/SemesterC/spl/assignment2/assigmnemnt2/src/main/java/bgu/spl/mics/application/input.json");
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
                JSONObject currAgentJson = (JSONObject) jaSquad.get(i);
                String name = (String)currAgentJson.get("name");
                String serNum = (String) currAgentJson.get("serialNumber");
                Agent a = new Agent(serNum, name);
                agents[i] = a;
            }
            Squad.getInstance().load(agents);

            //---------Services---------
            JSONObject jaServices = (JSONObject)jsonObject.get("services");

            //---------M---------
            long counterM = (Long)jaServices.get("M");
            for (int i=0; i<counterM; i++) {
                M newM = new M("M"+(i+1));
            }

            //---------Moneypenny---------
            long counterMoneypenny = (Long)jaServices.get("Moneypenny");
            for (int i=0; i<counterMoneypenny; i++) {
                Moneypenny newMoneypenny = new Moneypenny("Moneypenny"+(i+1));
            }

            //---------אtime---------
            long progDuration = (Long)jaServices.get("time");
            TimeService timeService = new TimeService((int)progDuration);

            //---Intelligence---
            JSONArray jaIntelligence = (JSONArray)jaServices.get("intelligence");
            for(int i=0; i< jaIntelligence.size(); i++)
            {
                Intelligence currIntelegence = new Intelligence("Intellegence" + (i+1));
                List<MissionInfo> currIntellegenceMissions = new LinkedList<>();
                JSONObject jaCurrIntelligence = (JSONObject)jaIntelligence.get(i);
                JSONArray jaMissions = (JSONArray) jaCurrIntelligence.get("missions");
                for(int j =0; j<jaMissions.size(); j++)
                {
                    JSONObject currSingleMission = (JSONObject)jaMissions.get(j);
                    long duration = (Long)currSingleMission.get("duration");
                    long timeExpired = (Long) currSingleMission.get("timeExpired");
                    long timeIssued = (Long) currSingleMission.get("timeIssued");
                    String gadget = (String)currSingleMission.get("gadget");
                    String name = (String)currSingleMission.get("name");
                    List<String> serialAgentsNumbers = new LinkedList<>();
                    JSONArray jaAgentsNumbers = (JSONArray) currSingleMission.get("serialAgentsNumbers");
                    for(int k=0; k<jaAgentsNumbers.size(); k++)
                    {
                        serialAgentsNumbers.add((String)jaAgentsNumbers.get(i));
                    }
                    MissionInfo currMission = new MissionInfo(name, serialAgentsNumbers, gadget, (int)timeIssued, (int)timeExpired, (int)duration);
                    currIntellegenceMissions.add(currMission);
                }
                currIntelegence.loadMissions(currIntellegenceMissions);
            }







        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
