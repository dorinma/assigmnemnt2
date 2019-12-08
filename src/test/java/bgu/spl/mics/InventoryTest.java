package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    Inventory inv;

    @BeforeEach
    public void setUp() {
        inv = new Inventory();
        inv.load(new String[]{"Sky Hook", "X-ray Glasses"});
    }

    @AfterEach
    public void tearDown() {
        inv = null;
    }

    @Test
    public void test_GetItem1() {
        boolean exists = inv.getItem("Sky Hook");
        assertTrue(exists);
    }

    @Test
    public void test_GetItem2() {
        boolean exists = inv.getItem("X-ray Glasses");
        assertTrue(exists);
    }

    @Test
    public void test_GetItem3() {
        boolean exists = inv.getItem("Dagger shoe");
        assertFalse(exists);
    }

    @Test
    public void test_GetInstance() {
        Inventory inv2 = new Inventory();
        assertSame(inv, inv2);
    }
}
