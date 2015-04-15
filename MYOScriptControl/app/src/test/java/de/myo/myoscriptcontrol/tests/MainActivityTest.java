package de.myo.myoscriptcontrol.tests;

import android.content.Intent;

import com.thalmic.myo.Pose;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;
import org.robolectric.shadows.ShadowToast;

import java.util.List;

import de.myo.myoscriptcontrol.listmanagement.gesturemanagement.GestureItem;

import de.myo.myoscriptcontrol.GestureScriptManager;
import de.myo.myoscriptcontrol.activities.MainActivity;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;
import de.myo.myoscriptcontrol.gesturerecording.MYOStatus;
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
        List<GestureItem> list = GestureScriptManager.getInstance().getGestureList();

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

    // TKi 04.04.2015
    @Test
    public void testStatus(){

        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity mainActivity = mainController.create().start().resume().get();

        mainActivity.OnUpdateStatus(MYOStatus.LOCKED);
        mainActivity.OnPose(Pose.DOUBLE_TAP);
        assertThat(mainActivity.getStatus(), equalTo(MYOStatus.IDLE));

        mainActivity.OnPose(Pose.FIST);
        assertThat(mainActivity.getStatus(), equalTo(MYOStatus.IDLE));

        mainActivity.OnPose(Pose.WAVE_OUT);
        assertThat(mainActivity.getStatus(), equalTo(MYOStatus.IDLE));

        mainActivity.OnPose(Pose.FINGERS_SPREAD);
        assertThat(mainActivity.getStatus(), equalTo(MYOStatus.LOCKED));

    }

    // TKi 04.04.2015
    @Test
    public void testExecutingMode(){
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity mainActivity = mainController.create().start().resume().get();

        mainActivity.OnUpdateStatus(MYOStatus.IDLE);
        assertThat(mainActivity.getExecutionMode(), equalTo(true));

        mainActivity.OnUpdateStatus(MYOStatus.LOCKED);
        assertThat(mainActivity.getExecutionMode(), equalTo(false));

        mainActivity.OnPose(Pose.DOUBLE_TAP);
        assertThat(mainActivity.getExecutionMode(), equalTo(true));

        mainActivity.OnPose(Pose.FINGERS_SPREAD);
        assertThat(mainActivity.getExecutionMode(), equalTo(false));
    }

    // TKi 04.04.2015
    @Test
    public void testPatternExecuting(){
        MainActivity targetActivity = setupActivity(null);
        GesturePattern expectedPattern = new GesturePattern();
        expectedPattern.add(GridPosition.POS_CENTER);

        targetActivity.OnUpdateStatus(MYOStatus.IDLE);
        targetActivity.setExecutionMode(true);
        targetActivity.setCurrentPosition(GridPosition.POS_CENTER);
        targetActivity.OnPose(Pose.FIST);

        assertThat(targetActivity.getPattern().toString(), equalTo(expectedPattern.toString()));

        expectedPattern.clear();
        targetActivity.OnPose(Pose.WAVE_OUT);

        assertThat(targetActivity.getPattern().toString(), equalTo(expectedPattern.toString()));
    }

    // TKi 04.04.2015
    public static MainActivity setupActivity(GesturePattern pattern) {
        if(pattern == null) {
            pattern = new GesturePattern();
        }
        Intent intent = new Intent().putExtra("pattern", pattern.toString());
        return Robolectric.buildActivity(MainActivity.class).withIntent(intent).create().resume().visible().get();
    }

}
