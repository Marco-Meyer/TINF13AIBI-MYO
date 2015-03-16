package de.myo.myoscriptcontrol.tests;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleJUnitTest {
    @Test
    public void junitTestTest() {
        assertThat(true, is(true));
    }
}

