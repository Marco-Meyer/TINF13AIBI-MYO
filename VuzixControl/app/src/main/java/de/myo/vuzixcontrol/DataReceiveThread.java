package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
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
    private Context mContext;
    private byte[] buffer;

    public DataReceiveThread(Context context, BluetoothSocket socket) {
        mContext = context;
        mBluetoothSocket = socket;
        buffer = new byte[10];
        try {
            mInputStream = mBluetoothSocket.getInputStream();
        } catch (IOException e) {

        }
    }

    public void run() {
        while (true) {
            try {
                mInputStream.read(buffer);
                evaluateInput();

            } catch (IOException e) {
                break;
            }
        }
    }

    private void evaluateInput() throws IOException {
        String command = null;
        try {
            command = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Integer keyCode = CommandManager.getCommandforString(command);
        if(keyCode != null) {
            mBluetoothSocket.close();
            ((VuzixControlApplication)mContext).OnReceiveCommand(command, keyCode);
        }
    }
}
