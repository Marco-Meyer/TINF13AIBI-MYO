package de.myo.vuzixcontrol;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by felix on 17.04.2015.
 */
public class VuzixControlApplication extends Application {

    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;

    public static String mUUIDString = "457807c0-4897-11df-9879-0800200c9a66";

    private static int SHOW_MESSAGE = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == SHOW_MESSAGE) {
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void scanForAvailableConnection() {
        try {
            new ConnectThread(this, "VuzixControl", UUID.fromString(mUUIDString)).start();
        } catch (Exception e) {
            scanForAvailableConnection();
        }
    }

    public void OnReceiveCommand(String command, int keyCode) {
        Message msg = new Message();
        msg.what = SHOW_MESSAGE;
        msg.obj = command;
        mHandler.sendMessage(msg);
        CommandExecutionService.startWithAction(this, CommandExecutionService.ACTION_INJECT_KEYEVENT, keyCode);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
