package de.myo.vuzixcontrol;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AbstractMenuActivity {

    private static int REQUEST_ENABLE_BT = 7;

    private void enableBluetooth() {
        BluetoothAdapter adapter = ((VuzixControlApplication)getApplication()).getBluetoothAdapter();
        if(!adapter.isEnabled()) {
            requestEnableBluetooth();
        }
        else {
            ((VuzixControlApplication)getApplication()).scanForAvailableConnection();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectButton = (Button) findViewById(getSelectButtonID());
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntentForMenuItem());
            }
        });

        enableBluetooth();
        findViewById(getBackgroundImageViewID()).setBackgroundResource(images.get(index));

    }


    public void requestEnableBluetooth() {
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
            ((VuzixControlApplication)getApplication()).scanForAvailableConnection();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getBackgroundImageViewID() {
        return R.id.wombat;
    }

    @Override
    protected int getSelectButtonID() {
        return R.id.selectButton;
    }
}
