package de.myo.myoscriptcontrol.tests;


import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;
import de.myo.myoscriptcontrol.MainActivity;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(CustomRobolectricTestRunner.class)
public class RobolectricTest {
    @Test
    public void testTest() {
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        assertThat(activity, notNullValue());
    }
}

