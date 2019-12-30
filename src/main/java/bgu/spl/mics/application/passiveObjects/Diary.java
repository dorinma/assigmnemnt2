package bgu.spl.mics.application.passiveObjects;
import org.json.simple.JSONArray;

import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	//Retrieves the single instance of this class.

	private List<Report> reports;
	//static AtomicInteger totalMissions = new AtomicInteger();
	private int total;

	private static class SingletonDiaryHolder{
		private static Diary instance = new Diary();
	}

	public Diary(){
		reports = new LinkedList<>();
		total = 0;
	}

	public static Diary getInstance() { return SingletonDiaryHolder.instance; }

	public List<Report> getReports() {
		return this.reports;
	}

	//adds a report to the diary
	public void addReport(Report reportToAdd){
		if (reportToAdd != null)
			this.reports.add((reportToAdd));
	}


	//return the total number of received missions (executed / aborted) be all the M-instances
	//public AtomicInteger getTotal(){
	public int getTotal(){
		return this.total;
		//return this.totalMissions;
	}

	public void incrementTotal(){
		//this.totalMissions.incrementAndGet();
		this.total++;
	}

	public void printToFile(String filename){
		JSONArray list = new JSONArray();
		for (int i = 0;i<getInstance().reports.size();i++)
		{
			JSONObject obj = new JSONObject();
			obj.put("missionName", getInstance().reports.get(i).getMissionName());
			obj.put("m", getInstance().reports.get(i).getM());
			obj.put("moneypenny", getInstance().reports.get(i).getMoneypenny());
			JSONArray listAgentsSerialNumbers = new JSONArray();
			for (int j =0;j<getInstance().reports.get(i).getAgentsSerialNumbers().size();j++)
			{
				listAgentsSerialNumbers.add(getInstance().reports.get(i).getAgentsSerialNumbers().get(j));
			}
			obj.put("agentsSerialNumbers", listAgentsSerialNumbers);
			JSONArray listAgentsNames = new JSONArray();
			for (int j =0;j<getInstance().reports.get(i).getAgentsNames().size();j++)
			{
				listAgentsNames.add(getInstance().reports.get(i).getAgentsNames().get(j));
			}
			obj.put("agentsNames", listAgentsNames);
			obj.put("gadgetName", getInstance().reports.get(i).getGadgetName());
			obj.put("qTime", getInstance().reports.get(i).getQTime());
			obj.put("timeIssued", getInstance().reports.get(i).getTimeIssued());
			obj.put("timeCreated", getInstance().reports.get(i).getTimeCreated());

			list.add(obj);
		}

		JSONObject objTotal = new JSONObject();
		String t = Integer.toString(this.total);
		objTotal.put("Total missions", t);
		list.add(objTotal);


		try (FileWriter file = new FileWriter(filename)) {
			file.write(list.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
