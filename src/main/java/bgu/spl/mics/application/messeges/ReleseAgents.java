package bgu.spl.mics.application.messeges;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.LinkedList;
import java.util.List;

public class ReleseAgents implements Event<Boolean> {

    private List<String> agentsNumbers;

    public ReleseAgents(List<String> serialAgentsNumbers) {
        this.agentsNumbers = serialAgentsNumbers;
    }

    public List<String> GetSerialNumbers() { return agentsNumbers; }

}
