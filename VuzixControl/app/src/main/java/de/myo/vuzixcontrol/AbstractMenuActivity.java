package de.myo.vuzixcontrol;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class AbstractMenuActivity extends ActionBarActivity {

    protected static int KEY_ENTER = 66;
    protected static int KEY_FORWARD = 22;
    protected static int KEY_BACK = 21;
    protected int index = 0;

    protected ArrayList<Integer> images = setupImages();
    protected HashMap<Integer, Class> activities = setupActivityHashMap();

    protected ArrayList<Integer> setupImages() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.mainmenu_button1);
        list.add(R.drawable.mainmenu_button2);
        list.add(R.drawable.mainmenu_button3);
        return list;
    }

    protected HashMap<Integer,Class> setupActivityHashMap() {
        HashMap<Integer, Class> activities = new HashMap<>();
        activities.put(0, ExampleActivity1.class);
        activities.put(1, ExampleActivity2.class);
        activities.put(2, ExampleActivity3.class);
        return activities;
    }

    protected Intent getIntentForMenuItem() {
        return new Intent(this, activities.get(index));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEY_ENTER) {
            findViewById(getSelectButtonID()).callOnClick();
        }
        if(keyCode == KEY_BACK) {
            moveLeft();
        }
        if(keyCode == KEY_FORWARD) {
            moveRight();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

    protected void moveLeft() {
        decrementIndex();
        findViewById(getBackgroundImageViewID()).setBackgroundResource(images.get(index));
    }

    protected void moveRight() {
        incrementIndex();
        findViewById(getBackgroundImageViewID()).setBackgroundResource(images.get(index));
    }

    private void incrementIndex() {
        if(index < images.size() - 1) {
            index++;
        }
        else {
            index = 0;
        }
    }

    private void decrementIndex() {
        if(index > 0) {
            index--;
        }
        else {
            index = images.size() - 1;
        }
    }

    protected int getSelectButtonID() {
        return R.id.menu_selectButton;
    }

    protected int getBackgroundImageViewID() {
        return R.id.menu_background;
    }
}
