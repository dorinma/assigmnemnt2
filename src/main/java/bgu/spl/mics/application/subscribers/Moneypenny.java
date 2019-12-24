package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.AgentsAvailableEvent;
import bgu.spl.mics.application.messeges.GadgetsAvailableEvent;
import bgu.spl.mics.application.messeges.TerminateBroadcast;
import bgu.spl.mics.application.messeges.TickBroadcast;
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
	private int currTick = 0;

	public Moneypenny(String name) {
		super(name); //name is serialNumber
		//this.serialNumber = serialNumber;
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
			LinkedList <String> agentList = event.getAgents();
			boolean allAgentsAvialible = Squad.getInstance().getAgents(agentList);

			if (allAgentsAvialible)
			{
				MessageBrokerImpl.getInstance().complete(event, currTick);
			}
			else
				MessageBrokerImpl.getInstance().complete(event, -1);

		});

	}

}
