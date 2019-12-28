package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messeges.TerminateBroadcast;
import bgu.spl.mics.application.messeges.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int duration;
	private int currTick;
	public static int time;

	public TimeService(int duration) {
		super("TimeService");
		this.duration = duration;
		this.currTick = 0;
		time = duration;
	}

	public int getDuration() { return duration; }

	@Override
	protected void initialize() { run(); }

	@Override
	public void run() {
		try {
			while (currTick < duration) {
				TickBroadcast tickBroadcast = new TickBroadcast(currTick);
				getSimplePublisher().sendBroadcast(tickBroadcast);
				Thread.sleep(100);
				currTick++;
//				System.out.println("--------- TICK " + currTick + " ---------" );
			}
			TerminateBroadcast terminateBroadcast = new TerminateBroadcast();
			getSimplePublisher().sendBroadcast(terminateBroadcast);
		}
		catch (InterruptedException ex) {}
	}
}
