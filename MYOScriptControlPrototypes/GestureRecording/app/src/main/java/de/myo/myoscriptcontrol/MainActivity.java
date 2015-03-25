package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.scanner.ScanActivity;


public class MainActivity extends ActionBarActivity {
    private static Hub mMyoHub;
    private static DeviceListener mMyoListener;
    private String mAppStatus;
    private TextView mLogText;

    public static void removeGestureRecognitionListener(){
        mMyoHub.removeListener(mMyoListener);
    }

    public static void setGestureRecognitionListener(){
        mMyoHub.addListener(mMyoListener);
    }

    public static Hub getMyoHub(){
        return mMyoHub;
    }

    private void startConnectMyoActivity(){
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);
    }

    private void initializeViews(){
        mLogText = (TextView)findViewById(R.id.textLog);
    }

    public void initMyoHub(){
        mMyoHub = Hub.getInstance();
        if (!mMyoHub.init(this)) {
            Toast.makeText(this.getApplicationContext(),
                    "could not initialize the hub.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }
        mMyoHub.setLockingPolicy(Hub.LockingPolicy.STANDARD);
        mMyoHub.setSendUsageData(false);
        mMyoHub.attachToAdjacentMyo();
    }

    private void doLogEntry(String text){
        mLogText.setText(text + "\n" + mLogText.getText());
    }

    private void initMyoListener(){
        mMyoListener = new AbstractDeviceListener() {
            @Override
            public void onConnect(Myo myo, long timestamp) {
                super.onConnect(myo, timestamp);
            }

            @Override
            public void onDisconnect(Myo myo, long timestamp) {
                doLogEntry("Myo Disconnected!");
            }

            @Override
            public void onPose(Myo myo, long timestamp, Pose pose) {
//                mPose = pose;
//                onPoseMain(pose);
            }

            @Override
            public void onUnlock(Myo myo, long timestamp) {
                super.onUnlock(myo, timestamp);
                doLogEntry("Unlock");
            }

            @Override
            public void onDetach(Myo myo, long timestamp) {
                super.onDetach(myo, timestamp);
                doLogEntry("Detach");
            }

            @Override
            public void onAttach(Myo myo, long timestamp) {
                super.onAttach(myo, timestamp);
                doLogEntry("Attach");
            }

            @Override
            public void onLock(Myo myo, long timestamp) {
                super.onLock(myo, timestamp);
                doLogEntry("Lock");
            }

            @Override
            public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
                super.onGyroscopeData(myo, timestamp, gyro);
//                mGyro = gyro;
            }

            @Override
            public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
                super.onAccelerometerData(myo, timestamp, accel);
//                mAccel = accel;
            }

            @Override
            public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
                super.onOrientationData(myo, timestamp, rotation);
//                mOrientation = rotation;
            }
        };
        mMyoHub.addListener(mMyoListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("MYO Script Control");
        initMyoHub();
        initializeViews();
        initMyoListener();
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
            intent.putExtra("item", "hallo");
            startActivity(intent);
            return true;
        } else if (id == R.id.action_script_manager) {
            return true;
        } else if (id == R.id.action_connect_myo){
            startConnectMyoActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
