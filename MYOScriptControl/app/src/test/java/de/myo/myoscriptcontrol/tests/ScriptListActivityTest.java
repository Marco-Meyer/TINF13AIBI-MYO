package de.myo.myoscriptcontrol.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import java.util.List;

import de.myo.myoscriptcontrol.GestureItem;
import de.myo.myoscriptcontrol.GestureListActivity;
import de.myo.myoscriptcontrol.GestureScriptManager;
import de.myo.myoscriptcontrol.MainActivity;
import de.myo.myoscriptcontrol.ScriptItem;
import de.myo.myoscriptcontrol.ScriptListActivity;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by MMy on 22.03.2015.
 */
@RunWith(CustomRobolectricTestRunner.class)
public class ScriptListActivityTest {
    @Test
    public void testDeleteItem() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity mainActivity = mainController.create().start().resume().get();
        ActivityController<ScriptListActivity> listController = Robolectric.buildActivity(ScriptListActivity.class);
        ScriptListActivity listActivity = listController.create().start().resume().get();
        List<ScriptItem> list = GestureScriptManager.getInstance().getScriptList();
        ScriptItem testItem = new ScriptItem();
        assertThat(list.size(), is(0));
        list.add(testItem);
        assertThat(list.size(), is(1));
        listActivity.deleteItem(testItem);
        assertThat(list.size(), is(0));
    }
}
