package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBluetoothSocket;
    private ConnectThread mConnectThread;
    private Handler mHandler;
    public static String mUUIDString = "457807c0-4897-11df-9879-0800200c9a66";

    private static int KEY_ENTER = 66;
    private static int KEY_FORWARD = 22;
    private static int KEY_BACK = 21;
    private static int REQUEST_ENABLE_BT = 7;
    private static int CONNECTION_ESTABLISHED = 0;
    private static int COMMAND_RECEIVED = 1;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEY_ENTER) {
            scanForAvailableConnection();
        }
        if(keyCode == KEY_BACK) {
            cancelConnectionScan();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void scanForAvailableConnection() {
        try {
            if(mConnectThread == null) {
                if(mBluetoothAdapter.isEnabled()) {
                    mConnectThread = new ConnectThread(this, "vuzix", UUID.fromString(mUUIDString));
                    mConnectThread.start();
                    Toast.makeText(getApplicationContext(), "scanning for available connection", Toast.LENGTH_LONG).show();
                }
                else {
                    requestEnableBluetooth();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "scan failed: " + e.getMessage() + ", press enter to try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void cancelConnectionScan() {
        if(mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
            Toast.makeText(getApplicationContext(), "connection process stopped", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == CONNECTION_ESTABLISHED) {
                    Toast.makeText(getApplicationContext(), "successfully connected to device: " + mBluetoothSocket.getRemoteDevice().getAddress(), Toast.LENGTH_LONG).show();
                    return false;
                }
                if(msg.what == COMMAND_RECEIVED) {
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                    return false;
                }
                return false;
            }
        });

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        setContentView(R.layout.activity_main);
        findViewById(R.id.wombat).setBackgroundResource(R.drawable.wombat_vuzix);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        scanForAvailableConnection();
    }

    private void requestEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            scanForAvailableConnection();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnReceiveCommand(String command) {
        Message msg = new Message();
        msg.what = COMMAND_RECEIVED;
        msg.obj = command;
        mHandler.sendMessage(msg);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
