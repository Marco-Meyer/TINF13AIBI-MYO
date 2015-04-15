package de.myo.myoscriptcontrol.tests.gesturerecordingtest;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import de.myo.myoscriptcontrol.activities.GestureRecordActivity;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by felix on 25.03.2015.
 */

@RunWith(CustomRobolectricTestRunner.class)
public class GestureRecordActivityTest {
    @Test
    public void patternLoaded() {
        GesturePattern pattern = new GesturePattern();
        pattern.add(GridPosition.POS_CENTER);
        pattern.add(GridPosition.POS_NORTH);
        GestureRecordActivity target = setupActivity(pattern);
        assertThat(target.getPattern().asJsonArray().toString(), equalTo(pattern.asJsonArray().toString()));
    }

    public static GestureRecordActivity setupActivity(GesturePattern pattern) {
        if(pattern == null) {
            pattern = new GesturePattern();
        }
        Intent intent = new Intent().putExtra("pattern", pattern.asJsonArray().toString());
        return Robolectric.buildActivity(GestureRecordActivity.class).withIntent(intent).create().resume().get();
    }
}
