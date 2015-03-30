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
 * Created by Daniel on 19.03.2015.
 */
public class ScriptListActivity extends ActionBarActivity {
    private static final int ADD_SCRIPT_REQUEST = 1;
    private static final int EDIT_SCRIPT_REQUEST = 2;
    private ArrayList<ScriptItem> mScriptList = new ArrayList<ScriptItem>();
    private ScriptItemListViewAdapter mListViewAdapter;
    private ListView mListView;
    private ScriptItem mSelectedItemToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_list);
        mScriptList = GestureScriptManager.getInstance().getScriptList();

        mListViewAdapter = new ScriptItemListViewAdapter(this, mScriptList);
        mListView = (ListView)findViewById(R.id.listViewScripts);
        mListView.setAdapter(mListViewAdapter);
        registerForContextMenu(mListView);

        initializeListeners();
    }

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listViewScripts) {
            menu.setHeaderTitle("Skript");
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

    public void deleteItem(ScriptItem longClickedItem){
        String scriptName = longClickedItem.getName();
        mScriptList.remove(longClickedItem);
        mListViewAdapter.notifyDataSetChanged();
        GestureScriptManager.getInstance().saveToJsonFile();
        Toast.makeText(getApplicationContext(), scriptName + " wurde gelöscht", Toast.LENGTH_LONG).show();
    }

    private void addItem(){
        Intent intent = new Intent(ScriptListActivity.this, ScriptEditActivity.class);
        intent.putExtra("addOrEdit", "add");
        startActivityForResult(intent, ADD_SCRIPT_REQUEST);
    }

    private void editItem(ScriptItem longClickedItem){
        Intent intent = new Intent(ScriptListActivity.this, ScriptEditActivity.class);
        intent.putExtra("addOrEdit", "edit");
        try {
            intent.putExtra("item", longClickedItem.asJsonObject().toString(2));
            startActivityForResult(intent, EDIT_SCRIPT_REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_script_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_script) {
            addItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_SCRIPT_REQUEST && resultCode == RESULT_OK){
            try {
                String scriptItemResult = data.getStringExtra("resultItem");
                ScriptItem item = new ScriptItem(new JSONObject(scriptItemResult));
                mScriptList.add(item);
                mListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GestureScriptManager.getInstance().saveToJsonFile();
        } else if (requestCode == EDIT_SCRIPT_REQUEST && resultCode == RESULT_OK){
            try {
                String scriptItemResult = data.getStringExtra("resultItem");
                mSelectedItemToEdit.insertJsonData(new JSONObject(scriptItemResult));
                mListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GestureScriptManager.getInstance().saveToJsonFile();
        }
    }


}
