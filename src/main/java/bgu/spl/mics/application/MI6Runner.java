package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */

public class MI6Runner {


    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        List<Thread> allProgThreads = new LinkedList<>();
        long counterThreads = 0;

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
                System.out.println(a.getSerialNumber());
                agents[i] = a;
            }
            Squad.getInstance().load(agents);

            //---------Services---------
            JSONObject jaServices = (JSONObject)jsonObject.get("services");

            counterThreads = calculateCounter(jaServices);
            CountDownLatch countDownLatch = new CountDownLatch((int)counterThreads);

            //---------Q---------
            Q singleProgQ = new Q("Q", countDownLatch);
            allProgThreads.add(new Thread(singleProgQ));

            //---------M---------
            long counterM = (Long)jaServices.get("M");
            for (int i=0; i<counterM; i++) {
                M newM = new M("M"+(i+1), countDownLatch);
                allProgThreads.add(new Thread(newM));
            }

            //---------Moneypenny---------
            long counterMoneypenny = (Long)jaServices.get("Moneypenny");
            for (int i=0; i<counterMoneypenny; i++) {
                Moneypenny newMoneypenny = new Moneypenny("Moneypenny"+(i+1), countDownLatch);
                allProgThreads.add(new Thread(newMoneypenny));
            }

            //---Intelligence---
            JSONArray jaIntelligence = (JSONArray)jaServices.get("intelligence");
            for(int i=0; i< jaIntelligence.size(); i++)
            {
                Intelligence currIntelegence = new Intelligence("Intellegence" + (i+1), countDownLatch);
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
                      //  if(!serialAgentsNumbers.contains())
                        serialAgentsNumbers.add((String)jaAgentsNumbers.get(k));
                    }
                    System.out.println(name);
                    for (String s: serialAgentsNumbers
                         ) {
                        System.out.println(s);
                    }
                    MissionInfo currMission = new MissionInfo(name, serialAgentsNumbers, gadget, (int)timeIssued, (int)timeExpired, (int)duration);
                    currIntellegenceMissions.add(currMission);
                }
                currIntelegence.loadMissions(currIntellegenceMissions);
                allProgThreads.add(new Thread(currIntelegence));
            }

            //---------אtime---------
            long progDuration = (Long)jaServices.get("time");
            TimeService timeService = new TimeService((int)progDuration);
           // ((LinkedList<Thread>) allProgThreads).addFirst(new Thread(timeService));

            for (Thread t : allProgThreads) {
                t.start();
/*                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                }*/
            }
            Thread timeServiceThread = new Thread(timeService);
            timeServiceThread.start();


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long calculateCounter(JSONObject jaServices){
        long m = (long)jaServices.get("M");
        long counterMoneypenny = (Long)jaServices.get("Moneypenny");
        JSONArray jaIntelligence = (JSONArray)jaServices.get("intelligence");
        int counterIntellignce = jaIntelligence.size();
        int counterQ = 1;
        int total = (int) ((int) m+counterMoneypenny+counterIntellignce+counterQ);
        System.out.println("there are " +total+ (" int this counter"));
        return total;
    }
}
