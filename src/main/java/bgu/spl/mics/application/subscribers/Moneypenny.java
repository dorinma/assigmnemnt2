package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private String serialNumber;
	private CountDownLatch countDownLatch;
	//private static Integer id = 1;
	private int currTick = 0;

	public Moneypenny(String name, CountDownLatch countDownLatch) {
		super(name);
		this.countDownLatch = countDownLatch;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			currTick = tickBroadcast.getTick();
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			terminate();
		});
		subscribeEvent(AgentsAvailableEvent.class, (event) -> {
			LinkedList <String> agentList = (LinkedList)event.getAgents();
			for (String s:agentList
				 ) {
				System.out.println(s);
			}
			boolean allAgentsAvialible = Squad.getInstance().getAgents(agentList);
			System.out.println(agentList.size());

			if (allAgentsAvialible)
			{
				//Squad.getInstance().sendAgents(agentList, 2);
				System.out.println(",ponnypenny is about to send avialibel agents");
				getSimplePublisher().sendEvent(new SendAgentsEvent(Squad.getInstance().getAgentsNames(agentList)));
				//Pair<Integer, LinkedList<Agent>> futMoneypenny = new Pair(currTick, Squad.getInstance().getAgentsNames(agentList));
				SendAgentsEvent sendAgentsEvent = new SendAgentsEvent(agentList);
				complete(sendAgentsEvent, getName());
			}
			else {
				System.out.println(",ponnypenny is about to NOT AV agents");

				complete(event, "NOT EVEYONE ARE EVILIABLE");
			}
		});
		countDownLatch.countDown();
	}

}
