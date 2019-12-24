package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
//import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private int currTick = 0;
	private int duration; //for entire program

	public M(String name, int duration) { //TODO IN THE MAIN
		super(name); // TODO fix names. in more places
		this.duration = duration;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			terminate();
		});

		subscribeEvent(MissionRecievedEvent.class, (event)-> {
			MissionInfo currMission = event.getMissionInfo();
			if (currMission != null)
			{
				String gdg = currMission.getGadget();
				Future<Integer> futGadget = getSimplePublisher().sendEvent(new GadgetsAvailableEvent(gdg));
				if (futGadget.get() != -1)
				{
					LinkedList<String> serialAgent = (LinkedList<String>) currMission.getSerialAgentsNumbers();
					Future<Integer> futAgent = getSimplePublisher().sendEvent(new AgentsAvailableEvent(serialAgent));
					if (futAgent.get() != -1)
					{
						if (currMission.getTimeIssued() == currTick && currMission.getDuration() + currTick <= this.duration)
						{
							SendAgentsEvent sendAgents = new SendAgentsEvent(currMission.getSerialAgentsNumbers());
							if(sendAgents != null) {
								Future<List<String>> agentsNames = getSimplePublisher().sendEvent(sendAgents);
								Report currReport = new Report();
								currReport.setMissionName(currMission.getMissionName());
								currReport.setM("M" + this.getName());//TODO???
								currReport.setMoneypenny(futAgent.get().toString());
								currReport.setAgentsSerialNumbersNumber(serialAgent);
								currReport.setAgentsNames(agentsNames.get());
								currReport.setGadgetName(gdg);
								currReport.setQTime(futGadget.get());
								currReport.setTimeIssued(currMission.getTimeIssued());
								currReport.setQTime(futGadget.get());
								currReport.setTimeCreated(currTick);
								Diary.getInstance().addReport(currReport);
							}
						}
						else {
							ReleseAgents releseAgents = new ReleseAgents(currMission.getSerialAgentsNumbers());
						}
					}
				}
			}

		});
	}
}
