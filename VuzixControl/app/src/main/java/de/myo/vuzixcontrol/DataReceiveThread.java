package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by felix on 02.04.2015.
 */
public class DataReceiveThread extends Thread {

    private static final int MESSAGE_READ = 42;
    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private Handler mHandler;

    public DataReceiveThread(BluetoothSocket socket) {
        mBluetoothSocket = socket;
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
                // Send the obtained bytes to the UI activity
                evaluateInput(buffer);

            } catch (IOException e) {
                break;
            }
        }
    }

    private void evaluateInput(byte[] buffer) {
        mHandler.obtainMessage(MESSAGE_READ, 0, -1, buffer)
                .sendToTarget();
    }
}
