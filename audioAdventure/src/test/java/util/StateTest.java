package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {
    @Test
    public void ensureStringFormat() {
        assertEquals(State.CheckPoint1.toString(), "CheckPoint1");
        assertEquals(State.CheckPoint2.toString(), "CheckPoint2");
        assertEquals(State.New.toString(), "New");
    }
}