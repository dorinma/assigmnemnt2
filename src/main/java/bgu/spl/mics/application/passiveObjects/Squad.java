package bgu.spl.mics.application.passiveObjects;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private static class SingletonHolder {
		private static Squad instance = new Squad();
	}

	private Map<String, Agent> agents;

	private Squad() {
		agents = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return  SingletonHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for(int i = 0; i < agents.length; i++) {
			this.agents.put(agents[i].getSerialNumber(), agents[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String s: serials
			 ) {
			Agent a = agents.get(s);
			if(a != null) {
				a.release();
				a.notifyAll();
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
	public boolean getAgents(List<String> serials) throws InterruptedException {
		Collections.sort(serials); //to avoid deadlock
		for (String s: serials
			 ) {
			Agent a = agents.get(s);
			if(a == null)
				return false;
			synchronized (a){
				while(!a.isAvailable())
					a.wait();
				a.acquire();
			}
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
    	List<String> names = new LinkedList<>();
		for (String s: serials
			 ) {
			names.add(agents.get(s).getName());
		}
        return names;
    }

}
