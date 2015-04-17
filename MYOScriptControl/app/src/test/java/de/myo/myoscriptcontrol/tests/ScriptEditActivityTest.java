package de.myo.myoscriptcontrol.tests;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

import de.myo.myoscriptcontrol.FileManager;
import de.myo.myoscriptcontrol.activities.MainActivity;
import de.myo.myoscriptcontrol.activities.ScriptEditActivity;
import de.myo.myoscriptcontrol.listmanagement.scriptmanagement.ScriptItem;
import de.myo.myoscriptcontrol.testutil.CustomRobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by DTH on 22.03.2015.
 */

@RunWith(CustomRobolectricTestRunner.class)
public class ScriptEditActivityTest {

    @Test
    public void testSaveScriptItem() {
        ActivityController<MainActivity> controllerMain = Robolectric.buildActivity(MainActivity.class);
        MainActivity mainActivity = controllerMain.create().start().resume().get();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("addOrEdit", "add");
        ScriptEditActivity activity = Robolectric.buildActivity(ScriptEditActivity.class).withIntent(intent).create().get();

        File file = new File("testfile.tst");
        FileOutputStream fOut = null;

        String testString = 123454754+"\n";
        try {
            fOut = new FileOutputStream(file);
            fOut.write(testString.getBytes());
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        activity.saveScriptItem(file);
        ScriptItem item = activity.getScriptItem();
        File destFile = new File(MainActivity.ScriptDir, item.getScriptFile());
        String fileString = "";
        try {
            fileString = FileManager.getStringFromFile(destFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        file.delete();

        assertThat(fileString, is(testString));
    }
}
