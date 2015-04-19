package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
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
    private Context mContext;

    public ConnectThread(Context context, String name, UUID uuid) throws IOException {
        BluetoothServerSocket tmp = null;
        mContext = context;
        try {
            tmp = ((VuzixControlApplication)mContext).getBluetoothAdapter().listenUsingRfcommWithServiceRecord(name, uuid);

        } catch (IOException e) {
            throw e;
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket;
        while (true) {
            try {
                Thread.sleep(50);
                socket = mmServerSocket.accept();
            } catch (Exception e) {
                ((VuzixControlApplication)mContext).OnReceiveCommand("error in ConnectThread: " + e.getMessage(), 0);
                continue;
            }
            if (socket != null) {
                DataReceiveThread receiveThread = new DataReceiveThread(mContext, socket);
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
