package de.myo.myoscriptcontrol;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GestureEditActivity extends ActionBarActivity {
    private static final int EDIT_PATTERN_REQUEST = 1;
    private static final int SHOW_PATTERN_REQUEST = 2;

    private GestureItem mGestureItem;
    private String mGestureItemString;
    private ImageButton buttonNameEdit, buttonScriptEdit, buttonPatternPlay, buttonPatternEdit;
    private TextView textViewName, textViewScript;

    private void loadPatternIntoGrid(GesturePattern pattern){
        int count = pattern.size();
        String[] patternList = new String[count];
        for (int i = 0; i < count; i++) {
            patternList[i] = pattern.get(i).toString();
        }
        GridView grid = (GridView)findViewById(R.id.gridViewGesturePattern);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1, patternList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        grid.setAdapter(arrayAdapter);
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
        buttonNameEdit = (ImageButton)findViewById(R.id.imageButtonGestureName);
        buttonScriptEdit = (ImageButton)findViewById(R.id.imageButtonEditGestureScript);
        buttonPatternEdit = (ImageButton)findViewById(R.id.imageButtonEditGesturePattern);
        buttonPatternPlay = (ImageButton)findViewById(R.id.imageButtonPlayGesturePattern);

        textViewName = (TextView)findViewById(R.id.textViewGestureName);
        textViewScript = (TextView)findViewById(R.id.textViewGestureScript);
        initializeListeners();
    }

    private void initializeListeners(){
        buttonNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertTextInput(mGestureItem.getName());
            }
        });
        buttonScriptEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "TODO: Show scripts", Toast.LENGTH_LONG).show();
            }
        });
        buttonPatternEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGestureRecordActivity(mGestureItem.getPattern());
            }
        });
        buttonPatternPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGestureShowActivity(mGestureItem.getPattern());
            }
        });
    }

    private void startGestureRecordActivity(GesturePattern pattern){
        Toast.makeText(getApplicationContext(), "TODO: GestureRecordActivity", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(GestureEditActivity.this, GestureRecordActivity.class);
//        intent.putExtra("pattern", pattern.asJsonArray().toString());
//        startActivityForResult(intent, EDIT_PATTERN_REQUEST);
    }

    private void startGestureShowActivity(ArrayList<GridPosition> pattern){
        Toast.makeText(getApplicationContext(), "TODO: Show pattern Activity", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), mGestureItem.getPattern().asJsonArray().toString(), Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(GestureEditActivity.this, GestureRecordActivity.class);
//        intent.putExtra("addOrEdit", "add");
//        startActivityForResult(intent, SHOW_PATTERN_REQUEST);
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
                Toast.makeText(getApplicationContext(), pattern.asJsonArray().toString(), Toast.LENGTH_LONG).show();
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
        input.setText(text);
        alertDialogBuilder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = input.getText().toString();
                        textViewName.setText(text);
                        mGestureItem.setName(text);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        TextView name = (TextView)findViewById(R.id.textViewGestureName);
        TextView script = (TextView)findViewById(R.id.textViewGestureScript);
        name.setText(gestureItem.getName());
        script.setText(gestureItem.getScript());
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
}
