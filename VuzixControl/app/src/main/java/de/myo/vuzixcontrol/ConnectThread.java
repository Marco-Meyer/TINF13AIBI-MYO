package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by felix on 02.04.2015.
 */
public class ConnectThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;
    private MainActivity mActivityContext;

    public ConnectThread(MainActivity activityContext, String name, UUID uuid) throws IOException {
        BluetoothServerSocket tmp = null;
        mActivityContext = activityContext;
        try {
            tmp = mActivityContext.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(name, uuid);

        } catch (IOException e) {
            throw e;
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket;
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (Exception e) {
                continue;
            }
            if (socket != null) {
                DataReceiveThread receiveThread = new DataReceiveThread(mActivityContext, socket);
                receiveThread.start();
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}
