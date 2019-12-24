package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messeges.GadgetsAvailableEvent;
import bgu.spl.mics.application.messeges.MissionRecievedEvent;
import bgu.spl.mics.application.messeges.TerminateBroadcast;
import bgu.spl.mics.application.messeges.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static java.util.Comparator.comparing;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private List<MissionInfo> missionList;
	private int currTick;

	public Intelligence(String name) {
		super(name);
		Collections.sort(missionList, comparing(MissionInfo::getTimeIssued));
	}

	@Override
	protected void initialize() throws InterruptedException {

		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			terminate();
		});

		while (missionList.size() > 0 && missionList.get(0).getTimeIssued() <= currTick)
		{
			MissionInfo currMission = missionList.get(0);
			missionList.remove(0);
			MissionRecievedEvent mre = new MissionRecievedEvent(currMission);
			Future<Report> f = getSimplePublisher().sendEvent(mre);
		}
	}

}
