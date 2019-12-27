package bgu.spl.mics.application.messeges;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event {

    private List<String> serialAgentsNames;

    public  SendAgentsEvent(List <String> serialAgentsNames) {
        this.serialAgentsNames = serialAgentsNames;
    }

    public List<String> getAgentsNames(){
        return serialAgentsNames;
    }

}
