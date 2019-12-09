package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    Future future;

    @BeforeEach
    public void setUp(){
        future = new Future();
    }

    @AfterEach
    public void tearDown(){
        future = null;
    }

    @Test
    void test_Get1() {
        future.resolve("done");
        assertEquals("done", future.get());
    }

    @Test
    void test_Get2() {
        future.resolve("done");
        assertNotEquals("", future.get());
    }

    @Test
    void test_Resolve() {
        future.resolve("done");
        assertTrue(future.isDone());
    }

    @Test
    void test_IsDone() {
        assertFalse(future.isDone());
    }
}
