package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.Console;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by felix on 02.04.2015.
 */
public class ConnectThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;
    private MainActivity mActivityContext;

    public ConnectThread(MainActivity activityContext, String name, UUID uuid) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        mActivityContext = activityContext;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mActivityContext.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket;
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            if (socket != null) {
                mActivityContext.setBluetoothSocket(socket);
                cancel();
                break;
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}
