package bgu.spl.mics.application.messeges;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

public class SendAgentsEvent implements Event<List<String>> {

    private List<String> agents;
    private int time;

    public SendAgentsEvent(List <String> serialAgents, int time) {
        this.agents = serialAgents;
        this.time = time;
    }

    public List<String> getSerials  (){
        return agents;
    }

    public int getTime(){return time;}
}
