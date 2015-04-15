package de.myo.myoscriptcontrol.tests.gesturerecordingtest;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.myo.myoscriptcontrol.listmanagement.gesturemanagement.GestureItem;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Tabea on 25.03.2015.
 */
@RunWith(CustomRobolectricTestRunner.class)
public class GestureItemTest {

    //TKi 26.03.2015
    @Test
    public void testEqualPattern(){

        boolean equalPattern;

        GestureItem gestureItem = new GestureItem();
        gestureItem.setName("TestGestureItem");
        gestureItem.setScript("TestScript");
        GesturePattern gesturePattern = new GesturePattern();
        gesturePattern.add(GridPosition.POS_SOUTH_EAST);
        gestureItem.setPattern(gesturePattern);

        GestureItem emptyGestureItem = new GestureItem();
        emptyGestureItem.setName("EmptyGestureItem");
        GesturePattern emptyGesturePattern = new GesturePattern();
        emptyGestureItem.setPattern(emptyGesturePattern);

        GestureItem wrongSizeItem = new GestureItem();
        wrongSizeItem.setName("WrongSizePattern");
        GesturePattern wrongSizePattern = new GesturePattern();
        wrongSizePattern.add(GridPosition.POS_SOUTH_EAST);
        wrongSizePattern.add(GridPosition.POS_SOUTH);
        wrongSizeItem.setPattern(wrongSizePattern);

        GestureItem wrongElementsItem = new GestureItem();
        wrongSizeItem.setName("WrongElementsPattern");
        GesturePattern wrongElementsPattern = new GesturePattern();
        wrongElementsPattern.add(GridPosition.POS_EAST);
        wrongElementsItem.setPattern(wrongElementsPattern);

        equalPattern = gestureItem.equalPattern(emptyGesturePattern);
        assertThat(equalPattern, is(false));

        equalPattern = gestureItem.equalPattern(wrongSizePattern);
        assertThat(equalPattern, is(false));

        equalPattern = gestureItem.equalPattern(wrongElementsPattern);
        assertThat(equalPattern, is(false));

        equalPattern = gestureItem.equalPattern(gesturePattern);
        assertThat(equalPattern, is(true));

    }
}
