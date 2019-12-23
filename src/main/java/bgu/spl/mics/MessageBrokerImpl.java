package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBrokerImpl instance = null;
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> subscribers;
	private ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> topics;
	private ConcurrentHashMap<Event, Future> futures;


	//Retrieves the single instance of this class
	public static synchronized MessageBroker getInstance() {
		if (instance == null)
		instance = new MessageBrokerImpl();
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {

		if(!topics.get(type).contains(m))
			topics.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if(futures.containsKey(e)){
			futures.get(e).resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (Subscriber sub: topics.get(b.getClass())
			 ) {
			subscribeBroadcast(b.getClass(), sub);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Subscriber sub = topics.get(e).poll();
		if (sub == null)
			return null;
		else {
			subscribers.get(sub).add(e);
			Future<T> future = new Future<>();
			futures.put(e, future);
			return future;
		}
	}

	@Override
	public void register(Subscriber m) {
		if(!subscribers.containsKey(m)) {
			subscribers.put(m, new ConcurrentLinkedQueue());
		}
	}

	@Override
	public void unregister(Subscriber m) {
		if(subscribers.containsKey(m)) {
			subscribers.remove(m);
		}
		//TODO Implement
		//remove m from topics- foreach??
	}

	@Override
	public Message awaitMessage(Subscriber m) {
		return subscribers.get(m).poll();
	}

}
