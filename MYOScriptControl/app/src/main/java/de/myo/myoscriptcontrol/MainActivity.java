package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.Hub;
import com.thalmic.myo.Pose;
import com.thalmic.myo.scanner.ScanActivity;

import java.io.File;
import java.util.ArrayList;

import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;
import de.myo.myoscriptcontrol.gesturerecording.ListenerTarget;
import de.myo.myoscriptcontrol.gesturerecording.*;

public class MainActivity extends ActionBarActivity implements ListenerTarget{

    private String ConfigDir;
    private File ConfigFile;
    public static String ScriptDir;
    public static Hub MYOHub;

    public static GestureScriptManager mManager;

    private static GestureRecordDeviceListener MyoListener = new GestureRecordDeviceListener();
    private RecordActivityStatus mStatus = RecordActivityStatus.UNKNOWN;
    private GesturePattern mPattern;
    private Pose mPose;
    private boolean mExecutionMode;
    private GridPosition mCurrentPosition;
    private GridPosition mLastPosition;

    private void initializeFiles(){
        ConfigDir = getMyoFileDir("config/");
        ConfigFile = new File(ConfigDir, "Config.json");
        ScriptDir = getMyoFileDir("scripts/");
    }

    private String getMyoFileDir(String folder) {
        String fileDirPath = getFilesDir().getAbsolutePath();
        File file = new File(fileDirPath + "/" + folder);
        file.mkdir();
        if (!file.isDirectory()) {
            throw new IllegalStateException("Folder couldn't be created");
            //instead, use new error handling here.
        }
        return file.getAbsolutePath();
    }

    private void initializeMYOHub() {
        MyoListener.addTarget(this);
        try {
            MYOHub = Hub.getInstance();

            if (!MYOHub.init(this)) {
                throw new Exception();
            }
            if(mStatus != RecordActivityStatus.DISCONNECTED || mStatus != RecordActivityStatus.UNKNOWN){
                initializeMYOListenerForHub(MYOHub);
            }
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Could not initialize MYO Hub", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeMYOListenerForHub(Hub hub){
        try{
            hub.addListener(MyoListener);
            hub.setLockingPolicy(Hub.LockingPolicy.STANDARD);
            if (hub.getConnectedDevices().size()==0){
                hub.attachToAdjacentMyo();
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Could not initialize MYO Listener", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFiles();
        GestureScriptManager.getInstance().setConfigFile(ConfigFile);
        mStatus = RecordActivityStatus.DISCONNECTED;
        initializeMYOHub();
        updateStatus();
    }

    // TKi 30.08.2015
    @Override
    protected void onResume() {
        super.onResume();
        mPose = Pose.UNKNOWN;
        updateStatus();
        initializeMYOHub();
    }

    // TKi 30.03.2015
    @Override
    protected void onPause() {
        super.onPause();
        MYOHub.removeListener(MyoListener);
        MyoListener.removeTarget(this);
        mExecutionMode = false;
    }

    // TKi 30.03.2015
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MYOHub.removeListener(MyoListener);
        MyoListener.removeTarget(this);
        MYOHub.shutdown();
        mExecutionMode = false;
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
        ArrayList<GestureItem> gestureList = GestureScriptManager.getInstance().getGestureList();
        int counterNoEqualGestureItem = 0;
        for(GestureItem gestureItem : gestureList){
            if(gestureItem.equalPattern(recordedPattern)){
                String scriptName = gestureItem.getScript();
                // executeScript(scriptName);
                Toast.makeText(getApplicationContext(), "Available Script: "+ scriptName , Toast.LENGTH_SHORT).show();
            }
            else{
                counterNoEqualGestureItem++;
                if(counterNoEqualGestureItem == gestureList.size()) {
                    Toast.makeText(getApplicationContext(), "No available script for the executed gesture combination.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // TKi 28.03.2015
    @Override
    public void OnPose(Pose pose) {
        mPose = pose;
        if(mPattern == null){
            mPattern = new GesturePattern();
        }
        if(mPose == Pose.DOUBLE_TAP) {
            OnUpdateStatus("IDLE");
        }
        if(mExecutionMode) {
            if(mPose == Pose.FIST) {
                mPattern.add(mCurrentPosition);
                mLastPosition = mCurrentPosition;
                String pattern = mPattern.toString();
                Toast.makeText(getApplicationContext(), pattern , Toast.LENGTH_SHORT).show();
            }
            if(mPose == Pose.FINGERS_SPREAD) {
                OnUpdateStatus("LOCKED");
                checkRecordedPatternForAvailableScript(mPattern);
            }
            if (mPose == Pose.WAVE_OUT) {
                mPattern.clear();
            }
        }
    }

    // TKi 28.03.2015
    @Override
    public void OnGridPositionUpdate(GridPosition position) {
        if(mExecutionMode) {
            mCurrentPosition = position;
        }
    }

    // TKi 28.03.2015
    @Override
    public void OnUpdateStatus(String status) {
        mStatus = RecordActivityStatus.valueOf(status);
        updateStatus();
    }

    // TKi 28.03.2015
    private void updateStatus(){
        ((TextView)findViewById(R.id.textViewMainStatus)).setText("Verbindungsstatus: " + mStatus.toString());
        if(mStatus==RecordActivityStatus.DISCONNECTED){
            ((ImageView) findViewById(R.id.imageViewMainStatus)).setImageResource(android.R.color.holo_red_light);
        }
        if(mStatus==RecordActivityStatus.UNSYNCED) {
            ((ImageView) findViewById(R.id.imageViewMainStatus)).setImageResource(android.R.color.holo_red_light);
        }
        if(mStatus==RecordActivityStatus.LOCKED) {
            ((ImageView) findViewById(R.id.imageViewMainStatus)).setImageResource(android.R.color.holo_orange_light);
            mExecutionMode = false;
        }
        if(mStatus==RecordActivityStatus.IDLE) {
            ((ImageView) findViewById(R.id.imageViewMainStatus)).setImageResource(android.R.color.holo_green_light);
            mExecutionMode = true;
        }
        if(mStatus==RecordActivityStatus.UNKNOWN){
            ((ImageView) findViewById(R.id.imageViewMainStatus)).setImageResource(android.R.color.holo_blue_light);
        }

    }
    // TKi 04.04.2015
    public GesturePattern getPattern() {
        return mPattern;
    }

    // TKi 04.04.2015
    public void setExecutionMode(boolean mExecutionMode) {
        this.mExecutionMode = mExecutionMode;
    }

    // TKi 04.04.2015
    public boolean getExecutionMode() {
        return mExecutionMode;
    }

    // TKi 04.04.2015
    public RecordActivityStatus getStatus() {
        return mStatus;
    }

    // TKi 04.04.2015
    public void setCurrentPosition(GridPosition gridPosition){ this.mCurrentPosition = gridPosition; }
}
