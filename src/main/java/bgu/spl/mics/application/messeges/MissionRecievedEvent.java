package bgu.spl.mics.application.messeges;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionRecievedEvent implements Event <Boolean> {

    private MissionInfo mission;

    public MissionRecievedEvent(MissionInfo missionInfo) {
        mission = missionInfo;
    }

    public MissionInfo getMissionInfo() {
        return mission;
    }
}
