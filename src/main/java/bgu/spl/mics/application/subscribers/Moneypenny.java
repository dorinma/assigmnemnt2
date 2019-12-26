package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.awt.*;
import java.util.LinkedList;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private String serialNumber;
	//private static Integer id = 1;
	private int currTick = 0;

	public Moneypenny(String name) {
		super(name);
		//id++;
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
			boolean allAgentsAvialible = Squad.getInstance().getAgents(agentList);
			if (allAgentsAvialible)
			{
				getSimplePublisher().sendEvent(new SendAgentsEvent(Squad.getInstance().getAgentsNames(agentList)));
				//Pair<Integer, LinkedList<Agent>> futMoneypenny = new Pair(currTick, Squad.getInstance().getAgentsNames(agentList));
				complete(event, getName());
			}
			else {
				complete(event, -1);
			}
		});

	}

}
