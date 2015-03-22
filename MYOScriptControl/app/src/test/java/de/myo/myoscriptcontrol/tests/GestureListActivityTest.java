package de.myo.myoscriptcontrol.tests;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import de.myo.myoscriptcontrol.GestureItem;
import de.myo.myoscriptcontrol.GestureListActivity;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;
import de.myo.myoscriptcontrol.MainActivity;


/**
 * Created by MMy on 22.03.2015.
 */
@RunWith(CustomRobolectricTestRunner.class)
public class GestureListActivityTest {
    @Test
    public void testDeleteItem() {
        ActivityController<MainActivity> mainController = Robolectric.buildActivity(MainActivity.class);
        MainActivity mainActivity = mainController.create().start().resume().get();
        ActivityController<GestureListActivity> listController = Robolectric.buildActivity(GestureListActivity.class);
        GestureListActivity listActivity = listController.create().start().resume().get();
        List<GestureItem> list = mainActivity.mManager.getGestureList();
        GestureItem testItem = new GestureItem();
        assertThat(list.size(), is(0));
        list.add(testItem);
        assertThat(list.size(), is(1));
        listActivity.deleteItem(testItem);
        assertThat(list.size(), is(0));
    }
}
