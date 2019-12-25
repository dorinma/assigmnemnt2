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

	public Intelligence() {
		super("");
		Collections.sort(missionList, comparing(MissionInfo::getTimeIssued));
	}

	public void setMissions(List<MissionInfo> missionList) {
		this.missionList = missionList;
	}

	@Override
	protected void initialize() throws InterruptedException {

		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();

			int count = 1;
			while(missionList.size() > 0 & count!=missionList.size())
			{
				for (MissionInfo info : missionList) {
					if (info.getTimeIssued() == currTick)
					{
						missionList.remove(info);
						MissionRecievedEvent mre = new MissionRecievedEvent(info);
						getSimplePublisher().sendEvent(mre);
					}
				}
				count++;
			}
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			terminate();
		});
	}

}
