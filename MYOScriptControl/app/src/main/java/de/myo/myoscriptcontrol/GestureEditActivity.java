package de.myo.myoscriptcontrol;

/**
 * Created by Daniel on 18.03.2015.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;

public class GestureEditActivity extends ActionBarActivity {
    private static final int EDIT_PATTERN_REQUEST = 1;
    private static final int SHOW_PATTERN_REQUEST = 2;

    private GestureItem mGestureItem;
    private String mGestureItemString;
    private ImageButton mButtonNameEdit, mButtonScriptEdit, mButtonPatternPlay, mButtonPatternEdit;
    private EditText mTextViewName, mTextViewScript;
    private GesturePatternGridViewAdapter mGridAdapter;

    private void loadPatternIntoGrid(GesturePattern pattern){
        GridView grid = (GridView)findViewById(R.id.gridViewGesturePattern);
        mGridAdapter = new GesturePatternGridViewAdapter(this, pattern);
        grid.setAdapter(mGridAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        initializeViews();
        String activityType = getIntent().getStringExtra("addOrEdit");
        chooseActivityType(activityType);

    }

    public void initializeViews(){
        mButtonNameEdit = (ImageButton)findViewById(R.id.imageButtonGestureName);
        mButtonScriptEdit = (ImageButton)findViewById(R.id.imageButtonEditGestureScript);
        mButtonPatternEdit = (ImageButton)findViewById(R.id.imageButtonEditGesturePattern);
//        mButtonPatternPlay = (ImageButton)findViewById(R.id.imageButtonPlayGesturePattern);

        mTextViewName = (EditText)findViewById(R.id.textViewGestureName);
        mTextViewScript = (EditText)findViewById(R.id.textViewGestureScript);
        initializeListeners();
    }

    private void initializeListeners() {
        mButtonNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertTextInput(mGestureItem.getName());
            }
        });
        mButtonScriptEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(GestureEditActivity.this);
                dialog.setContentView(R.layout.activity_script_list);

                ArrayList<ScriptItem> scriptList = MainActivity.mManager.getScriptList();
                ListView listView = (ListView) dialog.findViewById(R.id.listViewScripts);
                final ScriptItemListViewAdapter adapter = new ScriptItemListViewAdapter(GestureEditActivity.this, scriptList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ScriptItem item = adapter.getItem(position);
                        mGestureItem.setScript(item.getId().toString());
                        refreshViews(mGestureItem);
                        dialog.cancel();
                    }
                });
                dialog.setCancelable(true);
                dialog.setTitle("Skript auswählen");
                dialog.show();
            }
        });
        mButtonPatternEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGestureRecordActivity(mGestureItem.getPattern());
            }
        });
    }

    private void startGestureRecordActivity(GesturePattern pattern){
        Intent intent = new Intent(GestureEditActivity.this, GestureRecordActivity.class);
        intent.putExtra("pattern", pattern.asJsonArray().toString());
        startActivityForResult(intent, EDIT_PATTERN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PATTERN_REQUEST && resultCode == RESULT_OK){
            try {
                String gesturePatternResult = data.getStringExtra("resultPattern");
                JSONArray jsonPattern = new JSONArray(gesturePatternResult);
                GesturePattern pattern = new GesturePattern(jsonPattern);
                mGestureItem.setPattern(pattern);
                loadPatternIntoGrid(pattern);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlertTextInput(String text){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.activity_text_input_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.editTextAlertInput);
        TextView label = (TextView) promptView.findViewById(R.id.labelInput);
        label.setText("Name eingeben:");
        input.setHint("Name");
        input.setText(text);
        alertDialogBuilder
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = input.getText().toString();
                        mTextViewName.setText(text);
                        mGestureItem.setName(text);
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private void chooseActivityType(String type){
        switch (type){
            case "add":{
                addGesture();
                break;
            }
            case "edit":{
                editGesture();
                break;
            }
            default:{
                this.getSupportActionBar().setTitle("Fehler!");
                break;
            }
        }
    }

    private void addGesture(){
        this.getSupportActionBar().setTitle("Geste erstellen");
        mGestureItem = new GestureItem();
        refreshViews(mGestureItem);
    }

    private void editGesture(){
        this.getSupportActionBar().setTitle("Geste editieren");
        mGestureItemString = getIntent().getStringExtra("item");
        try {
            mGestureItem = new GestureItem(new JSONObject(mGestureItemString));
            refreshViews(mGestureItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshViews(GestureItem gestureItem){
        mTextViewName.setText(gestureItem.getName());
        try {
            UUID uuid = UUID.fromString(gestureItem.getScript());
            ScriptItem scriptItem = MainActivity.mManager.getScriptByUUID(uuid);
            mTextViewScript.setText(scriptItem.getName());
        } catch (NullPointerException|IllegalArgumentException e){
            mTextViewScript.setText(gestureItem.getScript());
        }
        loadPatternIntoGrid(gestureItem.getPattern());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gesture_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel_gesture) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.action_save_gesture){
            try {
                mGestureItemString = mGestureItem.asJsonObject().toString(2);
                Intent intent = new Intent();
                intent.putExtra("resultItem", mGestureItemString);
                setResult(RESULT_OK, intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public GestureItem getGestureItem() {
        return mGestureItem;
    }
}
