package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messeges.MissionRecievedEvent;
import bgu.spl.mics.application.messeges.TerminateBroadcast;
import bgu.spl.mics.application.messeges.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
	private CountDownLatch countDownLatch;
	private HashMap<Integer, MissionInfo> missionMap;

	public Intelligence(String name, CountDownLatch countDownLatch) {
		super(name);
		this.countDownLatch = countDownLatch;
		missionMap = new HashMap<>();
	}

	public void loadMissions(List<MissionInfo> missionList) {
		this.missionList = missionList;
		Collections.sort(missionList, comparing(MissionInfo::getTimeIssued));
		for (MissionInfo m : missionList) {
			missionMap.put(m.getTimeIssued(), m);
		}
	}

	@Override
	protected void initialize()  {

		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();

			if(missionMap.containsKey(currTick))
			{
				MissionInfo info = missionMap.get(currTick);
				System.out.println("the mission " + info.getMissionName() + " will published on tick " + currTick + " by " + this.getName());
				missionMap.remove(currTick);
				MissionRecievedEvent mre = new MissionRecievedEvent(info);
				getSimplePublisher().sendEvent(mre);
				//complete(mre, 1);
				System.out.println(this.getName() + " sent event " + mre.getMissionInfo().getMissionName());
			}
//			int count = 1;
//			while(!missionList.isEmpty() & count!=missionList.size())
//			{
//				for (MissionInfo info : missionList) {
//					if (info.getTimeIssued() == currTick)
//					{
//						System.out.println("the mission " + info.getMissionName() + " will published on tick " + currTick + " by " + this.getName());
//						missionList.remove(info);
//						MissionRecievedEvent mre = new MissionRecievedEvent(info);
//						getSimplePublisher().sendEvent(mre);
//						complete(mre, 1);
//						//notifyAll();
//						System.out.println(this.getName() + " sent event " + mre.getMissionInfo().getMissionName());
//					}
//				}
//				count++;
//			}
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			System.out.println("------------------->>>>>>>>>> "+ this.getName() + " terminated");
			terminate();
		});
		countDownLatch.countDown();
	}

}
