package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by felix on 02.04.2015.
 */
public class DataReceiveThread extends Thread {

    private static final int MESSAGE_READ = 42;
    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private MainActivity mActivityContext;

    public DataReceiveThread(MainActivity activityContext) {
        mActivityContext = activityContext;
        mBluetoothSocket = mActivityContext.getBluetoothSocket();
        try {
            mInputStream = mBluetoothSocket.getInputStream();
        } catch (IOException e) {

        }
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = mInputStream.read(buffer);
                evaluateInput(buffer);

            } catch (IOException e) {
                break;
            }
        }
    }

    private void evaluateInput(byte[] buffer) {
        if(buffer.length >= 22) {
            String command = null;
            try {
                command = new String(buffer, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mActivityContext.OnReceiveCommand(command);
        }
    }
}
