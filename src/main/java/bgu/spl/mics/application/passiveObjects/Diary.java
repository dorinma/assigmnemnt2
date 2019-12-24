package bgu.spl.mics.application.passiveObjects;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
	static AtomicInteger totalMissions = new AtomicInteger();


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
		if (reportToAdd != null)
		this.reports.add((reportToAdd)); }

	public void printToFile(String filename){
		//TODO: Implement this
	}

	//return the total number of received missions (executed / aborted) be all the M-instances
	public AtomicInteger getTotal(){
		return this.totalMissions;
	} //TODO check signiture

	public void incrementTotal(){
		this.totalMissions.incrementAndGet();
	}
}
