package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {

	private String missionName;
	private String Mid;
	private String MoneyPennyId;
	private List<String> agentsSerialNumbers;
	private List<String> agentsNames;
	private String gadgetName;
	private int timeIssued; //the time-tick when the mission was sent by an Intelligence publisher
	private int QTime; //the time-tick in which Q Received the GadgetAvailableEvent for that mission
	private int timeCreated; //the time-tick when the report has been created


	public String getMissionName() {
		return missionName;
	}


	//Sets the mission name.
	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}


	//Retrieves the M's id.
	public String getM() {
		return Mid;
	}


	//Sets the M's id.
	public void setM(String m) {
		this.Mid = m;
	}


	//Retrieves the Moneypenny's id.
	public String getMoneypenny() {
		return this.MoneyPennyId;
	}


	//Sets the Moneypenny's id.
	public void setMoneypenny(String moneypenny) {
		this.MoneyPennyId = moneypenny;
	}


	 //return The serial numbers of the agents.
	public List<String> getAgentsSerialNumbersNumber() {
		return agentsSerialNumbers;
	}


	//Sets the serial numbers of the agents.
	public void setAgentsSerialNumbersNumber(List<String> agentsSerialNumbersNumber) {
		for (String a : agentsSerialNumbersNumber) {
			agentsSerialNumbersNumber.add(a);
		}
	}

	//The agents names.
	public List<String> getAgentsNames() {
		return agentsNames;
	}


	//Sets the agents names.
	public void setAgentsNames(List<String> agentsNames) {
		for (String a : agentsNames) {
			agentsNames.add(a);
		}	}


	 //Retrieves the name of the gadget.
	public String getGadgetName() {
		return this.gadgetName;
	}

	//Sets the name of the gadget.
	public void setGadgetName(String gadgetName) {
		this.gadgetName = gadgetName;
	}


	//Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	public int getQTime() {
		return QTime;
	}


	//Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	public void setQTime(int qTime) {
		this.QTime = qTime;
	}


	//Retrieves the time when the mission was sent by an Intelligence Publisher.
	public int getTimeIssued() {
		return timeIssued;
	}

	//Sets the time when the mission was sent by an Intelligence Publisher.
	public void setTimeIssued(int timeIssued) {
		this.timeIssued = timeIssued;
	}


	//Retrieves the time-tick when the report has been created.
	public int getTimeCreated() {
		return timeCreated;
	}


	//Sets the time-tick when the report has been created.
	public void setTimeCreated(int timeCreated) {
		this.timeCreated = timeCreated;
	}
}
