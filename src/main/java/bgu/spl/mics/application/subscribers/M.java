package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	//private static Integer id = 1;
	private int currTick;
	private int serial;
	private CountDownLatch countDownLatch;
	private int duration; //for entire program
	//TODO NOR GOOOOOOOOOD

	public M (String name, CountDownLatch countDownLatch) {
		super(name);
		serial = Integer.parseInt(name.substring(1));
		this.countDownLatch = countDownLatch;
		currTick = 0;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();
			System.out.println("--------- TICK " + currTick + " ---------" + this.getName());
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			System.out.println("------------------->>>>>>>>>> "+ this.getName() + " terminated");
			terminate();
			System.out.println("--------- TERMINATE ON TICK " + currTick + " ---------");
		});

		subscribeEvent(MissionRecievedEvent.class, (event) -> {
			MissionInfo currMission = event.getMissionInfo();
			System.out.println(this.getName() + "subscribed to recive evnt" + event.getMissionInfo().getMissionName());
			if (currMission != null) {
				System.out.println("recived new mission" + currMission.getMissionName());
				//_______________________________________________________

				AgentsAvailableEvent availableAgents = new AgentsAvailableEvent(currMission.getSerialAgentsNumbers());
				Future<Integer> futAgent_MPserial = getSimplePublisher().sendEvent(availableAgents);
				if (futAgent_MPserial != null && futAgent_MPserial.get(currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS)!=-1) {
					String gdg = currMission.getGadget();
					GadgetsAvailableEvent availableGdg = new GadgetsAvailableEvent(gdg);
					Future<Integer> futGadget = getSimplePublisher().sendEvent(availableGdg);
					if (futGadget != null && futGadget.get(currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS) == 1) {
						SendAgentsEvent sendAgentsEvent = new SendAgentsEvent(currMission.getSerialAgentsNumbers(), currMission.getDuration());
						Future<List<String>> futSendingAgent = getSimplePublisher().sendEvent(sendAgentsEvent);
						if (futSendingAgent != null) {
							Report currReport = new Report();
							currReport.setMissionName(currMission.getMissionName());
							currReport.setM(serial);
							currReport.setMoneypenny(futAgent_MPserial.get(currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS)); //TODO TO CONVERT TO INT FROM STRING
							currReport.setAgentsSerialNumbersNumber(currMission.getSerialAgentsNumbers());
							currReport.setAgentsNames(futSendingAgent.get(currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS));
							currReport.setGadgetName(gdg);
							currReport.setTimeIssued(currMission.getTimeIssued());
							currReport.setQTime(futGadget.get(currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS));
							currReport.setTimeCreated(currTick);
							Diary.getInstance().addReport(currReport);

							//TODO needs to wait entire duration and after release agents

							complete(event, true);

						} else {
							complete(event, false);
						}
					} else {
						ReleseAgents releseAgents = new ReleseAgents(currMission.getSerialAgentsNumbers());
						getSimplePublisher().sendEvent(releseAgents);
						complete(event, false);
					}
				} else {
					complete(event, false);
				}
			}
				Diary.getInstance().incrementTotal();

		});
		countDownLatch.countDown();
	}
}