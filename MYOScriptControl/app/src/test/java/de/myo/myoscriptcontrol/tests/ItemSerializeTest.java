package de.myo.myoscriptcontrol.tests;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.myo.myoscriptcontrol.GestureItem;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.ScriptItem;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by DTH on 22.03.2015.
 */

@RunWith(CustomRobolectricTestRunner.class)
public class ItemSerializeTest {

    @Test
    public void activityTest() {
        ScriptItem sItem1 = new ScriptItem();
        sItem1.setDescription("blablablabla");
        sItem1.setName("testname");
        JSONObject jsonScItem1 = sItem1.asJsonObject();

        ScriptItem sItem2 = new ScriptItem(jsonScItem1);
        JSONObject jsonScItem2 = sItem2.asJsonObject();

        assertThat(jsonScItem1, is(jsonScItem2));

        GestureItem gItem1 = new GestureItem();
        gItem1.setPattern(new GesturePattern());
        gItem1.setName("testgesturename");
        gItem1.setScript(sItem2.getId().toString());
        JSONObject jsonGeItem1 = gItem1.asJsonObject();

        GestureItem gItem2 = new GestureItem(jsonGeItem1);
        JSONObject jsonGeItem2 = gItem2.asJsonObject();

        assertThat(jsonGeItem1, is(jsonGeItem2));
    }

}
