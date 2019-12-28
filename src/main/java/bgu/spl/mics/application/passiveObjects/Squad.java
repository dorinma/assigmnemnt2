package bgu.spl.mics.application.passiveObjects;
import com.sun.org.apache.xpath.internal.operations.Bool;

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

	private Map<String, Agent> agents;

	private static class SingletonSquadHolder{
		private static Squad instance = new Squad();
	}

	private Squad() {
		agents = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() { return SingletonSquadHolder.instance; }

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
	public synchronized void releaseAgents(List<String> serials){
		Collections.sort(serials);
		for (String s: serials) {
			System.out.println(s + "is the serial to chaeck");
			if(agents.get(s) != null)
				agents.get(s).release();
			System.out.println("RELEASE " + agents.get(s).getName());
		}
		this.notifyAll();
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time) throws InterruptedException {
		System.out.println("sleep for "+ time);
		Thread.currentThread().sleep(time*100);
		Collections.sort(serials);
		for (String s: serials) {
			agents.get(s).release();
			System.out.println("RELEASE " + agents.get(s).getName());
		}
		this.notifyAll();
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) throws InterruptedException {
		Boolean ans = true;
		Collections.sort(serials);
		for(String s : serials)
		{
			if(!agents.containsKey(s))
			{
				ans = false;
				for(String s2 : serials) {
					if (s2 == s)
						break;
					else
						agents.get(s2).release();
				}
				break;
			}
			else{
				while (!agents.get(s).isAvailable()) {
					//System.out.println("waiting for " + a.getName());
					this.wait();
				}
				agents.get(s).acquire();
				System.out.println(agents.get(s).getName() + " ACQUIRE ");
			}
		}
		return ans;
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
