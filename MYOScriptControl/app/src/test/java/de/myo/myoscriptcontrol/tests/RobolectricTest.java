package de.myo.myoscriptcontrol.tests;


import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.util.ActivityController;

import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;
import de.myo.myoscriptcontrol.MainActivity;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(CustomRobolectricTestRunner.class)
public class RobolectricTest {

    @Test
    public void activityTest() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        MainActivity activity = controller.create().start().resume().get();
        assertThat(activity, notNullValue());
    }

    public void shadowActivityTest() {
        MainActivity main = Robolectric.setupActivity(MainActivity.class);
        ShadowActivity shadowActivity = Robolectric.shadowOf(main);

        shadowActivity.getCallingActivity();
        shadowActivity.getNextStartedActivity();
        shadowActivity.getNextStartedService();

        assertThat(main, notNullValue());
    }
}

