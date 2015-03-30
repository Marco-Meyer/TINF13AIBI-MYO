package de.myo.myoscriptcontrol.tests;

import org.junit.Test;

import de.myo.myoscriptcontrol.GestureScriptManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Marco on 30.03.2015.
 */
public class GestureScriptManagerTest {
    @Test
    public void testSingletonProperties() {
        GestureScriptManager testManager1 = GestureScriptManager.getInstance();
        GestureScriptManager testManager2 = GestureScriptManager.getInstance();
        assertThat(testManager1, is(testManager2));
    }
}
