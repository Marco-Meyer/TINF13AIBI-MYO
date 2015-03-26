package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.thalmic.myo.Hub;
import com.thalmic.myo.scanner.ScanActivity;

import java.io.File;
import java.util.ArrayList;

import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;


public class MainActivity extends ActionBarActivity {

    private String ConfigDir;
    private File ConfigFile;
    public static String ScriptDir;
    public static Hub MYOHub;
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
        initializeMYOHub();
    }

    private void initializeMYOHub() {
        try {
            MYOHub = Hub.getInstance();
            if (!MYOHub.init(this)) {
                throw new Exception();
            }
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Could not initialize MYO Hub", Toast.LENGTH_LONG).show();
        }
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
        } else if (id == R.id.action_connect_myo) {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TKi 26.03.2015
    public void checkRecordedPatternForAvailableScript(GesturePattern recordedPattern){
        ArrayList<GestureItem> gestureList = mManager.getGestureList();
        for(GestureItem gestureItem : gestureList){
            if(gestureItem.equalPattern(recordedPattern)){
                String scriptName = gestureItem.getScript();
                Toast.makeText(getApplicationContext(), "Available Script: "+ scriptName , Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "No available script for execution", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
