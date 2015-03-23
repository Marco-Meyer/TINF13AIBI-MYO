package de.testsl4app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ComponentName;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button) findViewById(R.id.scriptButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pythonScriptPath = "/sdcard/sl4a/scripts/hello_world.py";
                Intent intent = buildStartInTerminalIntent(new File(pythonScriptPath));
                Log.d("SL4A Launcher", "The intent is " + intent.toString());
                startActivity(intent);
            }
        });
    }

    /**
     * Builds an intent that launches a script in a terminal.
     *
     * @param script
     *          the script to launch
     * @return the intent that will launch the script
     */
    public static Intent buildStartInTerminalIntent(File script) {
        final ComponentName componentName = Constants.SL4A_SERVICE_LAUNCHER_COMPONENT_NAME;
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setAction(Constants.ACTION_LAUNCH_FOREGROUND_SCRIPT);
        intent.putExtra(Constants.EXTRA_SCRIPT_PATH, script.getAbsolutePath());
        return intent;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
