package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messeges.GadgetsAvailableEvent;
import bgu.spl.mics.application.messeges.TerminateBroadcast;
import bgu.spl.mics.application.messeges.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.CountDownLatch;
//import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private int currTick = 0;
	private CountDownLatch countDownLatch;

	public Q(String name, CountDownLatch countDownLatch) {
		super(name);
		this.countDownLatch = countDownLatch;
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
		subscribeEvent(GadgetsAvailableEvent.class, (event) -> {
			String gdg = event.getGadget();
			if (Inventory.getInstance().getItem(gdg))
			{
				System.out.println("~~~" + this.getName() + " SEND GADGET " + gdg + " in tick: " + currTick);
				complete(event, currTick);
			}
			else {
				System.out.println("~~~" + this.getName() + " SEND GADGET " + gdg + " FAILED!!!");
				complete(event, -1);
			}
		});
		countDownLatch.countDown();
	}

}
