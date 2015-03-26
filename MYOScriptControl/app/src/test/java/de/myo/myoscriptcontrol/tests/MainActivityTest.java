package de.myo.myoscriptcontrol.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;
import org.robolectric.shadows.ShadowToast;

import java.util.List;

import de.myo.myoscriptcontrol.GestureItem;
import de.myo.myoscriptcontrol.MainActivity;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by TKi on 26.03.2015.
 */
@RunWith(CustomRobolectricTestRunner.class)
public class MainActivityTest {

    //TKi 26.03.2015
    @Test
    public void testCheckRecordedPatternForAvailableScript(){

        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity mainActivity = mainController.create().start().resume().get();
        List<GestureItem> list = mainActivity.mManager.getGestureList();

        GestureItem testItem = new GestureItem();
        testItem.setScript("TestScript");
        GesturePattern knownPattern = new GesturePattern();
        knownPattern.add(GridPosition.POS_SOUTH_EAST);
        testItem.setPattern(knownPattern);
        list.add(testItem);

        mainActivity.checkRecordedPatternForAvailableScript(knownPattern);

        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Available Script: TestScript"));

        GesturePattern unknownGesturePattern = new GesturePattern();
        unknownGesturePattern.add(GridPosition.POS_SOUTH);

        mainActivity.checkRecordedPatternForAvailableScript(unknownGesturePattern);

        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("No available script for execution"));

    }
}
