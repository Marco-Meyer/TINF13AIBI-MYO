package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private String ConfigDir;
    private File ConfigFile;
    private String ScriptDir;

    public static GestureScriptManager mManager;

    private void initializeFiles(){
        ConfigDir = getExternalFilesDir("config/").getAbsolutePath();
        ConfigFile = new File(ConfigDir, "Config.json");
        ScriptDir = getExternalFilesDir("scripts/").getAbsolutePath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFiles();
        mManager = new GestureScriptManager(ConfigFile);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gesture_manager) {
            Intent intent = new Intent(MainActivity.this, GestureListActivity.class);

            startActivity(intent);
            return true;
        } else if (id == R.id.action_script_manager) {
            Intent intent = new Intent(MainActivity.this, ScriptListActivity.class);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
