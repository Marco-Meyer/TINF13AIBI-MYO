package de.myo.myoscriptcontrol.activities;

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

import java.io.IOException;
import java.util.ArrayList;

import de.myo.myoscriptcontrol.listmanagement.gesturemanagement.GestureItem;
import de.myo.myoscriptcontrol.listmanagement.gesturemanagement.GestureItemListViewAdapter;
import de.myo.myoscriptcontrol.GestureScriptManager;
import de.myo.myoscriptcontrol.R;

/**
 * Created by Daniel on 16.03.2015.
 */

public class GestureListActivity extends ActionBarActivity {
    private static final int ADD_GESTURE_REQUEST = 1;
    private static final int EDIT_GESTURE_REQUEST = 2;
    private ArrayList<GestureItem> mGestureList = new ArrayList<GestureItem>();
    private GestureItemListViewAdapter mListViewAdapter;
    private ListView mListView;
    private GestureItem mSelectedItemToEdit;

    private void initializeListeners(){
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItemToEdit = mListViewAdapter.getItem(position);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItemToEdit = mListViewAdapter.getItem(position);
                editItem(mSelectedItemToEdit);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_list);
        mGestureList = GestureScriptManager.getInstance().getGestureList();
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
                editItem(mSelectedItemToEdit);
                break;
            case 1:
                deleteItem(mSelectedItemToEdit);
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
        try {
            if (requestCode == ADD_GESTURE_REQUEST && resultCode == RESULT_OK) {
                    String gestureItemResult = data.getStringExtra("resultItem");
                    GestureItem item = new GestureItem(new JSONObject(gestureItemResult));
                    mGestureList.add(item);
                    mListViewAdapter.notifyDataSetChanged();
                    GestureScriptManager.getInstance().saveToJsonFile();
            } else if (requestCode == EDIT_GESTURE_REQUEST && resultCode == RESULT_OK) {
                    String gestureItemResult = data.getStringExtra("resultItem");
                    mSelectedItemToEdit.insertJsonData(new JSONObject(gestureItemResult));
                    mListViewAdapter.notifyDataSetChanged();
                    GestureScriptManager.getInstance().saveToJsonFile();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            ErrorActivity.handleError(this, e.getMessage());
        }
    }

    private void deleteItem(GestureItem item){
        String gestureName = item.getName();
        mGestureList.remove(item);
        mListViewAdapter.notifyDataSetChanged();
        try {
            GestureScriptManager.getInstance().saveToJsonFile();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            ErrorActivity.handleError(this, e.getMessage());
        }
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
            ErrorActivity.handleError(this, e.getMessage());
        }
    }
}
