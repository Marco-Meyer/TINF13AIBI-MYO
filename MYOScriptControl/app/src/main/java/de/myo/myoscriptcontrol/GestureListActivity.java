package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Daniel on 16.03.2015.
 */

public class GestureListActivity extends ActionBarActivity {
    private static final int ADD_GESTURE_REQUEST = 1;
    private static final int EDIT_GESTURE_REQUEST = 2;
    private ArrayList<GestureItem> mGestureList = new ArrayList<GestureItem>();
    private GestureItemListViewAdapter mListViewAdapter;
    private ListView mListView;
    private GestureItem mLongClickedItem;
    private GestureScriptManager mGestureScriptManager;

    private void initializeListeners(){
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mLongClickedItem = mListViewAdapter.getItem(position);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_list);
        mGestureScriptManager = MainActivity.mManager;
        mGestureList = mGestureScriptManager.getGestureList();
        mListViewAdapter = new GestureItemListViewAdapter(this, mGestureList);
        mListView = (ListView)findViewById(R.id.listViewGestures);
        mListView.setAdapter(mListViewAdapter);
        registerForContextMenu(mListView);

        initializeListeners();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listViewGestures) {
            menu.setHeaderTitle("Geste");
            String[] menuItems = { "Bearbeiten", "Löschen" };
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();
        switch (menuItemIndex){
            case 0:
                editItem(mLongClickedItem);
                break;
            case 1:
                deleteItem(mLongClickedItem);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gesture_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_gesture) {
            addItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_GESTURE_REQUEST && resultCode == RESULT_OK){
            try {
                String gestureItemResult = data.getStringExtra("resultItem");
                GestureItem item = new GestureItem(new JSONObject(gestureItemResult));
                mGestureList.add(item);
                mListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mGestureScriptManager.saveToJsonFile();
        } else if (requestCode == EDIT_GESTURE_REQUEST && resultCode == RESULT_OK){
            try {
                String gestureItemResult = data.getStringExtra("resultItem");
                mLongClickedItem.insertJsonData(new JSONObject(gestureItemResult));
                mListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                    e.printStackTrace();
            }
            mGestureScriptManager.saveToJsonFile();
        }
    }

    public void deleteItem(GestureItem item){
        String gestureName = item.getName();
        mGestureList.remove(item);
        mListViewAdapter.notifyDataSetChanged();
        mGestureScriptManager.saveToJsonFile();
        Toast.makeText(getApplicationContext(), gestureName+" wurde gelöscht", Toast.LENGTH_LONG).show();
    }

    private void addItem(){
        Intent intent = new Intent(GestureListActivity.this, GestureEditActivity.class);
        intent.putExtra("addOrEdit", "add");
        startActivityForResult(intent, ADD_GESTURE_REQUEST);
    }

    private void editItem(GestureItem item){
        Intent intent = new Intent(GestureListActivity.this, GestureEditActivity.class);
        intent.putExtra("addOrEdit", "edit");
        try {
            intent.putExtra("item", item.asJsonObject().toString(2));
            startActivityForResult(intent, EDIT_GESTURE_REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
