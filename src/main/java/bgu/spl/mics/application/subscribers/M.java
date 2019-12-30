package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.publishers.TimeService;
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
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			System.out.println("------------------->>>>>>>>>> "+ this.getName() + " terminated");
			terminate();
		});

		subscribeEvent(MissionRecievedEvent.class, (event) -> {
			MissionInfo currMission = event.getMissionInfo();
			System.out.println("~~~" + this.getName() + " HANDLE MSG: " + event.getMissionInfo().getMissionName());
			if (currMission != null) {
				//_______________________________________________________

				AgentsAvailableEvent availableAgents = new AgentsAvailableEvent(currMission.getSerialAgentsNumbers());
				Future<Integer> futAgent_MPserial = getSimplePublisher().sendEvent(availableAgents);
				if (futAgent_MPserial != null) {
					Integer mpSerial = futAgent_MPserial.get();//currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS);
					if (mpSerial != -1) {
						String gdg = currMission.getGadget();
						GadgetsAvailableEvent availableGdg = new GadgetsAvailableEvent(gdg);
						Future<Integer> futGadget = getSimplePublisher().sendEvent(availableGdg);
						Integer qTime = futGadget.get();//currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS);
						if (futGadget != null && qTime != -1) {
							if (qTime + currMission.getDuration() < currMission.getTimeExpired()) {
								System.out.println("DUARIOTN" + currMission.getDuration());
								SendAgentsEvent sendAgentsEvent = new SendAgentsEvent(currMission.getSerialAgentsNumbers(), currMission.getDuration());
//								System.out.println("SEND AGENTS EVENT time: " + sendAgentsEvent.getTime());
								Future<List<String>> futSendingAgent = getSimplePublisher().sendEvent(sendAgentsEvent);
								List<String> agentsName = futSendingAgent.get(); //(currMission.getTimeExpired() - currTick, TimeUnit.MILLISECONDS);
								if (futSendingAgent != null) {
									System.out.println(this.getName() + " START REPORT: ");

									Report currReport = new Report();
									currReport.setMissionName(currMission.getMissionName());
									currReport.setM(serial);
									currReport.setMoneypenny(mpSerial);
									currReport.setAgentsSerialNumbers(currMission.getSerialAgentsNumbers());
									currReport.setAgentsNames(agentsName);
									currReport.setGadgetName(gdg);
									currReport.setTimeIssued(currMission.getTimeIssued());
									currReport.setQTime(qTime);
									System.out.println("qTime: " + qTime + " mission name: " + currMission.getMissionName());
									System.out.println("qTime + duration = " + (int)(qTime + currMission.getDuration()));
									currReport.setTimeCreated(qTime + currMission.getDuration());
									Diary.getInstance().addReport(currReport);
									complete(event, true);
								} //if (futSendingAgent != null)
								else {
									System.out.println(currMission.getMissionName() + " MISSION FAILED - NOT REPORTED!");
									complete(event, false);
								}
							} //if (qTime + currMission.getDuration() < currMission.getTimeExpired())
							else {
								System.out.println(currMission.getMissionName() + " MISSION FAILED - NOT REPORTED!");
								System.out.println("time issued: " + currMission.getTimeIssued() + " + mission duration: " + currMission.getDuration() +
										" > time expired: " + currMission.getTimeExpired());
								ReleseAgents releseAgents = new ReleseAgents(currMission.getSerialAgentsNumbers());
								getSimplePublisher().sendEvent(releseAgents);
								complete(event, false);
							}
						} //if (futGadget != null && qTime != -1)
						else {
							System.out.println(currMission.getMissionName() + " MISSION FAILED - NOT REPORTED!");
							System.out.println(gdg + "GADGET NOT FOUND - " + currMission.getMissionName() + " mission failed");
							ReleseAgents releseAgents = new ReleseAgents(currMission.getSerialAgentsNumbers());
							getSimplePublisher().sendEvent(releseAgents);
							complete(event, false);
						}
					}//if (mpSerial != -1)
				}//(futAgent_MPserial != null)
				else {
					System.out.println(currMission.getMissionName() + " MISSION FAILED - NOT REPORTED!");
					complete(event, false);
				}
			}//if (currMission != null)
			else
				System.out.println(currMission.getMissionName() + " MISSION NOT FOUND?!?!?!");


			Diary.getInstance().incrementTotal();


		});
		countDownLatch.countDown();
	}
}