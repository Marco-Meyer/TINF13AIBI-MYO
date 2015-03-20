package de.myo.myoscriptcontrol.tests;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleJUnitTest {
    @Test
    public void junitTestTest() {
        String testString = "blabla";
        assertThat(testString, is("blabla"));
    }

    @Test
    public void collectionAsserts() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("test1");
        list1.add("test2");

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("test1");
        list2.add("test2");

        assertThat(list1, equalTo(list2));
        assertThat(list1, hasItem("test2"));

    }
}

