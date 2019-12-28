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

	private static MessageBrokerImpl instance = new MessageBrokerImpl();
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> subscribers; //sub - events/broadcast msg
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> topics; //events(3) - sub
	private ConcurrentHashMap<Event, Future> futures; //each event waits for return value

	private static class SingletonHolder{
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private MessageBrokerImpl() {
		subscribers = new ConcurrentHashMap<>();
		topics = new ConcurrentHashMap<>();
		futures = new ConcurrentHashMap<>();
	}

	//Retrieves the single instance of this class
	public static MessageBroker getInstance() { //SYNC
		if (instance == null)
			instance = new MessageBrokerImpl();
		return instance;
	}

	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
			if(topics.containsKey(type))
				topics.get(type).add(m);
			else {
				ConcurrentLinkedQueue<Subscriber> subs = new ConcurrentLinkedQueue<>();
				subs.add(m);
				topics.put(type, subs);
			}
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {

		if (topics.containsKey(type))
			topics.get(type).add(m);

		if (!topics.containsKey(type)) {
				ConcurrentLinkedQueue<Subscriber> subs = new ConcurrentLinkedQueue<>();
				subs.add(m);
				topics.put(type, subs);
			}
	}

	@Override
	public synchronized  <T> void complete(Event<T> e, T result) {
		futures.get(e).resolve(result);
		futures.remove(e);
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		if (topics.get(b.getClass()) != null)
		{
			for (Subscriber sub : topics.get(b.getClass())) {
					subscribers.get(sub).add(b);
			}
			this.notifyAll();
		}
	}

	
	@Override
	public synchronized  <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = null;

		if (topics.containsKey(e.getClass())) {
			Subscriber sub = topics.get(e.getClass()).poll(); //we found the first sub that is registered to this type of event
			if (sub != null){
				subscribers.get(sub).add(e); //will go to subscribers map and add to this sub this event
				topics.get(e.getClass()).add(sub);
				future = new Future<>();
				futures.put(e, future);
				//System.out.println(sub.getName() + " is the one to recive this msg: " + e.toString());
				notifyAll();
			}
		}
	//	this.notifyAll();
		return future;
	}

	@Override
	public void register(Subscriber m) {
		if(!subscribers.containsKey(m)) {
			subscribers.put(m, new ConcurrentLinkedQueue<>()); //new subscriber
		}
	}

	@Override
	public void unregister(Subscriber m) {
		for (Class<? extends Message> msg : topics.keySet()) { //remove m from topics
			if(topics.get(msg).contains(m))
			topics.get(msg).remove(m);
		}
		if(subscribers.containsKey(m)) {
			subscribers.remove(m);
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		Message msg;
		synchronized (this)
		{
			while (subscribers.get(m).isEmpty())
			{
				//System.out.println(m.getName() + "WAIT for mission");
				this.wait();
			}
			msg = subscribers.get(m).poll();
		}
		return msg;
	}

}
