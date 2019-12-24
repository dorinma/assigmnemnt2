package bgu.spl.mics.application.passiveObjects;

import java.util.List;

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

	private static Diary instance=null; //for thread safe singleton
	private List<Report> reports;
	int totalMissions;


	public static synchronized Diary getInstance() {
		if (instance == null)
			instance = new Diary();
		return instance;
	}

	public List<Report> getReports() {
		return this.reports;
	}

	//adds a report to the diary
	public void addReport(Report reportToAdd){
		this.reports.add((reportToAdd)); }

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		//TODO: Implement this
	}


	//return the total number of received missions (executed / aborted) be all the M-instances
	public int getTotal(){
		return this.totalMissions;
	}

	public void incrementTotal(){
		this.totalMissions++;
	}
}
