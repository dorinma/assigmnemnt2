package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private int serialNumber;
	private CountDownLatch countDownLatch;
	//private static Integer id = 1;
	private int currTick = 0;

	public Moneypenny(String name, CountDownLatch countDownLatch) {
		super(name);
		serialNumber = Integer.parseInt(name.substring(10));
		this.countDownLatch = countDownLatch;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (tickBroadcast) -> {
			this.currTick = tickBroadcast.getTick();
		});
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroad) -> {
			System.out.println("------------------->>>>>>>>>> "+ this.getName() + " terminated");
			terminate();
		});

		if(this.serialNumber%2==0) {
			subscribeEvent(AgentsAvailableEvent.class, (event) -> {
				System.out.println("~~~" + this.getName() + " HANDLE MSG: " + event.getClass());
				List<String> agentList = (LinkedList) event.getAgents();
				System.out.println(this.getName() + "check if all Agents Avialible:");
				boolean allAgentsAvialible = Squad.getInstance().getAgents(agentList);

				if (allAgentsAvialible) {
					System.out.println("----->" + this.getName() + " Acquire agents for mission");
					List<String> result = Squad.getInstance().getAgentsNames(agentList);
					complete(event, this.serialNumber);
				} else {
					System.out.println("NOTTTTTTTTTT AVAILIBLEEEEEEEEEEEEEEEE");
					complete(event, -1);
				}
			});
		}
		else {
			subscribeEvent(SendAgentsEvent.class, (event) -> {
				System.out.println(this.getName() + "------>> send Agents Event");
				List<String> agentNames = Squad.getInstance().getAgentsNames(event.getSerials());
				Squad.getInstance().sendAgents(event.getSerials(), event.getTime());
				complete(event, agentNames);
			});

			subscribeEvent(ReleseAgents.class, (event) -> {
				System.out.println(this.getName() + "------>> Release Agents Event");
				Squad.getInstance().releaseAgents(event.GetSerialNumbers());
				System.out.println(this.getName() + " RELEASEDDDDD");
				complete(event, true);
			});

		}



		countDownLatch.countDown();
	}

}
