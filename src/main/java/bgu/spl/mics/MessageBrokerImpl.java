package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBrokerImpl instance = null;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subscribers; //sub - events/broadcast msg
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> topics; //events(3) - sub
	private ConcurrentHashMap<Event, Future> futures; //each event waits for return value

	private MessageBrokerImpl() {
		subscribers = new ConcurrentHashMap<>();
		topics = new ConcurrentHashMap<>();
		futures = new ConcurrentHashMap<>();
	}

	//Retrieves the single instance of this class
	public static synchronized MessageBroker getInstance() {
		if (instance == null)
			instance = new MessageBrokerImpl();
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
	//	synchronized (topics.get(type)) {
			if(topics.containsKey(type))
				topics.get(type).add(m);
			else {
				ConcurrentLinkedQueue<Subscriber> subs = new ConcurrentLinkedQueue<>();
				subs.add(m);
				topics.put(type, subs);
			}
	//	}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {

		if (topics.containsKey(type))
			topics.get(type).add(m);

	//	synchronized (topics.get(type)) {
			if (!topics.containsKey(type)) {
				ConcurrentLinkedQueue<Subscriber> subs = new ConcurrentLinkedQueue<>();
				subs.add(m);
				topics.put(type, subs);
			}
	//	}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if(futures.containsKey(e)){
			futures.get(e).resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (topics.get(b.getClass()) != null)
		{
			for (Subscriber sub : topics.get(b.getClass())) {
				try {
					subscribers.get(sub).put(b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		//	synchronized (topics.get(e.getClass())) {
		if (topics.get(e) != null) {
			Subscriber sub = topics.get(e).poll(); //we found the first sub that is registered to this type of event
			if (sub == null)
				return null;
			else {
				subscribers.get(sub).add(e); //will go to subscribers map and add to this sub this event
				topics.get(e).add(sub);
				Future<T> future = new Future<>();
				futures.put(e, future);
				return future;
			}
		}
		//	}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		if(!subscribers.containsKey(m)) {
			subscribers.put(m, new LinkedBlockingQueue<>()); //new subscriber
		}
	}

	@Override
	public void unregister(Subscriber m) {
		LinkedBlockingQueue<Message> mEvents = subscribers.get(m);
		for (Message msg : mEvents) { //remove m from topics
			topics.get(msg).remove(m);
		}

		if(subscribers.containsKey(m)) {
			subscribers.remove(m);
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subscribers.get(m).take();
	}

}
