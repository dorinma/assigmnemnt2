package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {

	private String missionName;
	private List<String> SerialAgentsNumbers;
	private String gadget;
	private int timeIssued;
	private int timeExpired;
	private int duration;

	public MissionInfo(String missionName, List serialAgentsNumbers, String gadget, int timeIssued, int timeExpired, int duration){
		this.missionName = missionName;
		this.SerialAgentsNumbers = serialAgentsNumbers;
		this.gadget = gadget;
		this.timeIssued = timeIssued;
		this.timeExpired = timeExpired;
		this.duration = duration;
	}

	//Sets the name of the mission.
    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    //Retrieves the name of the mission
	public String getMissionName() {
		return this.missionName;
	}


	//Sets the serial agent number
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
		for (String a: this.SerialAgentsNumbers)
		{
				serialAgentsNumbers.add(a);
		}
    }


	//Retrieves the serial agent number
	public List<String> getSerialAgentsNumbers() {
		return this.SerialAgentsNumbers;
	}

	//Sets the gadget name
    public void setGadget(String gadget) {
        this.gadget = gadget;
    }

	//Retrieves the gadget name
	public String getGadget() {
		return this.gadget;
	}

    //Sets the time the mission was issued in milliseconds
    public void setTimeIssued(int timeIssued) {
    	this.timeIssued = timeIssued;
    }


	//Retrieves the time the mission was issued in milliseconds.
	public int getTimeIssued() {
		return this.timeIssued;
	}

	//Sets the time that if it that time passed the mission should be aborted.
    public void setTimeExpired(int timeExpired) {
        this.timeExpired = timeExpired;
    }


    //Retrieves the time that if it that time passed the mission should be aborted.
	public int getTimeExpired() {
		return this.timeExpired;
	}


	//Sets the duration of the mission in time-ticks.
    public void setDuration(int duration) {
        this.duration = duration;
    }


    //Retrieves the duration of the mission in time-ticks
	public int getDuration() {
		return this.duration;
	}
}
