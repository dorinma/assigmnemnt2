package bgu.spl.mics.application.passiveObjects;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Comparator.comparing;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private static Squad instance=null; //for thread safe singleton
	private Map<String, Agent> agents;

	private Squad() {
		agents = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static synchronized Squad getInstance() {
		if (instance == null)
			instance = new Squad();
		return instance;	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public synchronized void load (Agent[] agents) { //TODO SYNC
		for(int i = 0; i < agents.length; i++) {
			agents[i].release();
			this.agents.put(agents[i].getSerialNumber(), agents[i]);
		}
		//Collections.checkedSortedMap()
	//TODO SORT THIS MAP OF AGETS BY KEY (THEIR SERIAL NUMBER)
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String s: serials) {
			Agent a = agents.get(s);
			if(a != null) {
				a.release();
				a.notifyAll(); //TO ASK
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {
		Thread.currentThread().sleep(time*100);
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) { //TODO syncho

		Collections.sort(serials);
		for (String s : serials) {
			System.out.println(s + "i am agent" );
			Agent a = agents.get(s);
			if (a == null){
				System.out.println(s + "i am null" );

			return false;}
			if (!agents.containsKey(a)){
				System.out.println("im am not exited");
				return false;
			}
		}

		for (String a : serials) {
			agents.get(a).acquire();
		}
		System.out.println("HADASSSSSSS");
		return true;
				/*Collections.sort(serials); //to avoid deadlock
		for (String s: serials) {
			Agent a = agents.get(s);
			if(a == null)
				return false;
			synchronized (a){
				while(!a.isAvailable())
					a.wait();
				a.acquire();
			}
		}
		return true; */
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
    	List<String> names = new LinkedList<>();
		for (String s: serials) {
			names.add(agents.get(s).getName());
		}
        return names;
    }

}
