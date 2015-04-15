package de.myo.myoscriptcontrol.tests;

import android.view.MenuItem;
import android.widget.ListView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import java.util.List;

import de.myo.myoscriptcontrol.GestureScriptManager;
import de.myo.myoscriptcontrol.activities.MainActivity;
import de.myo.myoscriptcontrol.R;
import de.myo.myoscriptcontrol.listmanagement.scriptmanagement.ScriptItem;
import de.myo.myoscriptcontrol.activities.ScriptListActivity;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;
import de.myo.myoscriptcontrol.testutil.MenuItemMock;

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

        List<ScriptItem> list = GestureScriptManager.getInstance().getScriptList();
        ScriptItem testItem = new ScriptItem();
        assertThat(list.size(), is(0));
        list.add(testItem);
        assertThat(list.size(), is(1));

        ActivityController<ScriptListActivity> listController = Robolectric.buildActivity(ScriptListActivity.class);
        ScriptListActivity listActivity = listController.create().start().resume().get();

        MenuItem mockItem = new MenuItemMock(1);
        ListView listView = (ListView) listActivity.findViewById(R.id.listViewScripts);
        listView.getOnItemLongClickListener().onItemLongClick(listView, listView, 0, 0);
        listActivity.onContextItemSelected(mockItem);
        assertThat(list.size(), is(0));
    }
}
