package de.myo.myoscriptcontrol.tests;

import org.json.JSONException;
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
        JSONObject jsonScItem1 = null;
        try {
            jsonScItem1 = sItem1.asJsonObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ScriptItem sItem2 = new ScriptItem(jsonScItem1);
        JSONObject jsonScItem2 = null;
        try {
            jsonScItem2 = sItem2.asJsonObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonScString1 = jsonScItem1.toString();
        String jsonScString2 = jsonScItem2.toString();
        boolean equal = jsonScString1.equalsIgnoreCase(jsonScString2);

        assertThat(equal, is(true));

        GestureItem gItem1 = new GestureItem();
        gItem1.setPattern(new GesturePattern());
        gItem1.setName("testgesturename");
        gItem1.setScript(sItem2.getId().toString());
        JSONObject jsonGeItem1 = null;
        try {
            jsonGeItem1 = gItem1.asJsonObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GestureItem gItem2 = new GestureItem(jsonGeItem1);
        JSONObject jsonGeItem2 = null;
        try {
            jsonGeItem2 = gItem2.asJsonObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonGeString1 = jsonGeItem1.toString();
        String jsonGeString2 = jsonGeItem2.toString();
        boolean equal2 = jsonGeString1.equalsIgnoreCase(jsonGeString2);

        assertThat(equal2, is(true));
    }

}
