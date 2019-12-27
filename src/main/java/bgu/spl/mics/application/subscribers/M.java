package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private static Integer id = 1;
	private int currTick;
	private CountDownLatch countDownLatch;
	private int duration; //for entire program
	//TODO NOR GOOOOOOOOOD

	public M (String name, CountDownLatch countDownLatch) {
		super(name);
		this.countDownLatch = countDownLatch;
		currTick = 0;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();
			System.out.println("we are one tick" + currTick);
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			terminate();
			System.out.println("terminate" + currTick);
		});

		subscribeEvent(MissionRecievedEvent.class, (event) -> {
			MissionInfo currMission = event.getMissionInfo();
			if (currMission != null) {
				System.out.println("recived new mission" + currMission.getMissionName());
				String gdg = currMission.getGadget();
				GadgetsAvailableEvent getGdg = new GadgetsAvailableEvent(gdg);
				Future<Integer> futGadget = getSimplePublisher().sendEvent(getGdg);



				System.out.println("gadget" + gdg + " was found");
				if (futGadget.get() != -1) {
						System.out.println("gadget" + gdg + "is avialible");
						AgentsAvailableEvent getAgents = new AgentsAvailableEvent(currMission.getSerialAgentsNumbers());



						Future<Integer> futAgent = getSimplePublisher().sendEvent(getAgents);



						System.out.println(futAgent.get());
						if (!futAgent.get().equals("")) { //!futAgent.get().equals("")
								System.out.println("agents for this mission are avialible !!!!!!!!!");
							//	if (currMission.getTimeIssued() == currTick ) { //TODO to decide what to do =====&& currMission.getDuration() + currTick <= this.duration
									subscribeEvent(SendAgentsEvent.class, (eventNames) -> {
										List<String> agentsNamesList = eventNames.getAgentsNames();
										System.out.println("ROW 11111111");
										if (agentsNamesList != null) {
											System.out.println("ROW 222222222");
											//Future<List<String>> agentsNames = getSimplePublisher().sendEvent(agentsNamesList);
											Report currReport = new Report();
											currReport.setMissionName(currMission.getMissionName());
											currReport.setM(this.id);
											currReport.setMoneypenny(futAgent.get());
											currReport.setAgentsSerialNumbersNumber(currMission.getSerialAgentsNumbers());
											currReport.setAgentsNames(agentsNamesList); //TODO to ask tomer
											currReport.setGadgetName(gdg);
											currReport.setTimeIssued(currMission.getTimeIssued());
											currReport.setQTime(futGadget.get());
											currReport.setTimeCreated(currTick);
											Diary.getInstance().addReport(currReport);

											//TODO needs to wait entire duration and after release agents
										}
									});
									//SendAgentsEvent sendAgents = new SendAgentsEvent(currMission.getSerialAgentsNumbers());

								} else {
									ReleseAgents releseAgents = new ReleseAgents(currMission.getSerialAgentsNumbers());
								}
							}
						}

		});
		countDownLatch.countDown();
	}
}