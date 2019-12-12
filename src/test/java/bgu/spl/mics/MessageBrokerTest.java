package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;



public class MessageBrokerTest {

    MessageBroker mBroker;
    Subscriber sub;

    @BeforeEach
    public void setUp() {
        mBroker = MessageBrokerImpl.getInstance();
        sub = new M();
        mBroker.register(sub);
        mBroker.subscribeEvent(EventStringImpl.class, sub);

    }

    @Test
    public void test_CompleteEvent() {
        Event<String> e = new EventStringImpl();
        Future future = mBroker.sendEvent(e);
        mBroker.complete(e, "done");
        assertEquals(future.get(), "done");
    }

    @Test
    public void test_IfRegistered() {
        Broadcast broadcast = new BroadcastIntegerImpl();
        mBroker.sendBroadcast(broadcast);
        mBroker.register(sub);
        mBroker.subscribeBroadcast((Class<? extends Broadcast<Number>>) BroadcastIntegerImpl.class, sub);
        try {
            assertEquals(broadcast, mBroker.awaitMessage(sub));
        }
        catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void test_IfNotRegistered() {
        Broadcast broadcast = new BroadcastIntegerImpl();
        mBroker.sendBroadcast(broadcast);
        try {
            mBroker.awaitMessage(sub);
            fail();
        }
        catch (InterruptedException e) {
            assert true;
        }
    }

}
