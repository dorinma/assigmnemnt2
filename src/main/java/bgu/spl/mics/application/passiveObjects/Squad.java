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
	public void load (Agent[] agents) {
		for(int i = 0; i < agents.length; i++) {
			agents[i].release();
			this.agents.put(agents[i].getSerialNumber(), agents[i]);
		}
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
		Collections.sort(serials);
		try {
			Thread.currentThread().sleep(100 * (long) time);
		}
		catch (InterruptedException e){}
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
	public boolean getAgents(List<String> serials) throws InterruptedException { //TODO I DELETED SYNC
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
				synchronized (this) {
					while (!agents.get(s).isAvailable()&&!Thread.currentThread().isInterrupted()) {
						try {
							//System.out.println("waiting for " + a.getName());
							this.wait();
						}
						catch (InterruptedException e){
							Thread.currentThread().interrupt();
							ans = false;
							//System.out.println("catch Exceptionnn!!!");
						}
					}
					//System.out.println(agents.get(s).getName() + " ACQUIRE ");
				}
				agents.get(s).acquire();
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
