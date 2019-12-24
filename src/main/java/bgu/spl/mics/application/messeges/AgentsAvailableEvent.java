package bgu.spl.mics.application.messeges;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.LinkedList;
import java.util.List;

public class AgentsAvailableEvent implements Event {

    private List<String> agentsSerialNumber;

    public AgentsAvailableEvent(List<String> agentsNums) {
        agentsSerialNumber = new LinkedList<>();
        this.agentsSerialNumber = agentsNums;
    }

    public List<String> getAgents() {
        return this.agentsSerialNumber;
    }
}
