package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    Squad squad;
    Agent a;
    Agent b;

    @BeforeEach
    public void setUp() {
        squad = new Squad();
        a = new Agent();
        b = new Agent();
        squad.load(new Agent[]{a, b});
    }
    @AfterEach
    void tearDown() {
        squad = null;
    }


    @Test
    void test_GetAgents1() {
        List<String> serNums = new LinkedList<>();
        serNums.add(a.getSerialNumber());
        serNums.add(b.getSerialNumber());

        boolean exist = squad.getAgents(serNums);
        assertTrue(exist);
    }

    @Test
    void test_GetAgents2() {
        List<String> serNums = new LinkedList<>();
        serNums.add(a.getSerialNumber());
        serNums.add("");

        boolean exist = squad.getAgents(serNums);
        assertFalse(exist);
    }

    @Test
    void test_GetAgents3() {
        List<String> serNums = new ArrayList<>();
        serNums.add("abc");
        serNums.add("def");

        boolean exist = squad.getAgents(serNums);
        assertFalse(exist);
    }

    @Test
    void test_GetAgentsNames1() {
        List<String> serNums = new ArrayList<>();
        serNums.add(a.getSerialNumber());

        List<String> names = squad.getAgentsNames(serNums);
        String name = names.get(0);
        assertSame(a.getName(), name);
    }

    @Test
    void test_GetAgentsNames2() {
        List<String> serNums = new ArrayList<>();
        serNums.add("abc");

        List<String> names = squad.getAgentsNames(serNums);
        assertNull(names);
    }

    @Test
    public void test_GetInstance() {
        Squad sq2 = new Squad();
        assertSame(squad, sq2);
    }
}