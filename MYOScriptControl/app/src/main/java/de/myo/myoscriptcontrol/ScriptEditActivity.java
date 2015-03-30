package de.myo.myoscriptcontrol;

/**
 * Created by Daniel on 19.03.2015.
 */

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ScriptEditActivity extends ActionBarActivity {
    private int IMPORT_SCRIPT_REQUEST = 0;

    private ScriptItem mScriptItem;
    private String mScriptItemString;
    private File mImportedScriptFile;
    private ImageButton mButtonNameEdit, mButtonDescriptionEdit, mButtonStartScript, mButtonScriptSearch;
    private EditText mTextViewName, mTextViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_edit);
        initializeViews();
        String activityType = getIntent().getStringExtra("addOrEdit");
        chooseActivityType(activityType);
    }

    public ScriptItem getScriptItem(){
        return mScriptItem;
    }

    public void initializeViews(){
        mButtonNameEdit = (ImageButton)findViewById(R.id.imageButtonScriptName);
        mButtonDescriptionEdit = (ImageButton)findViewById(R.id.imageButtonEditDescription);
        mButtonStartScript = (ImageButton)findViewById(R.id.imageButtonStartScript);
        mButtonScriptSearch = (ImageButton)findViewById(R.id.imageButtonImportScript);

        mTextViewName = (EditText)findViewById(R.id.textViewScriptName);
        mTextViewDescription = (EditText)findViewById(R.id.textViewDescription);
        initializeListeners();
    }

    private void initializeListeners(){
        mButtonNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertTextInputName(mScriptItem.getName());
            }
        });
        mButtonDescriptionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertTextInputDescription(mScriptItem.getDescription());
            }
        });
        mButtonStartScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "TODO: Test script", Toast.LENGTH_LONG).show();
                startScript();
            }
        });
        mButtonScriptSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScriptEditActivity.this, FileExplorerActivity.class);
                startActivityForResult(intent,IMPORT_SCRIPT_REQUEST);
            }
        });
    }

    private void startScript(){
        if (neededPackagesExist()){
            //TODO if fileexists
            File file = new File(MainActivity.ScriptDir ,mScriptItem.getScriptFile());
            Intent intent = buildStartSL4A(file);
            startActivity(intent);
        } else {
            //TODO
        }
    }

    private Intent buildStartSL4A(File file){
        final ComponentName componentName = SL4AConstants.SL4A_SERVICE_LAUNCHER_COMPONENT_NAME;
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setAction(SL4AConstants.ACTION_LAUNCH_BACKGROUND_SCRIPT);
        intent.putExtra(SL4AConstants.EXTRA_SCRIPT_PATH, file.getAbsolutePath());
        return intent;
    }

    private boolean neededPackagesExist(){
        final PackageManager pm = getPackageManager();
        boolean androidScriptingExists = false;
        boolean pythonForAndroidExists = false;
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages){
            if (packageInfo.packageName.equalsIgnoreCase("com.googlecode.android_scripting")){
                androidScriptingExists = true;
            }
            if (packageInfo.packageName.equalsIgnoreCase("com.googlecode.pythonforandroid")){
                pythonForAndroidExists = true;
            }
        }
        return androidScriptingExists & pythonForAndroidExists;
    }

    private void showAlertTextInputName(String text){
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
                        mScriptItem.setName(text);
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

    private void showAlertTextInputDescription(String text){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.activity_text_input_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.editTextAlertInput);
        TextView label = (TextView) promptView.findViewById(R.id.labelInput);
        label.setText("Beschreibung eingeben:");
        input.setHint("Beschreibung");
        input.setLines(3);
        input.setMaxLines(5);
        input.setText(text);
        alertDialogBuilder
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = input.getText().toString();
                        mTextViewDescription.setText(text);
                        mScriptItem.setDescription(text);
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
                addScript();
                break;
            }
            case "edit":{
                editScript();
                break;
            }
            default:{
                this.getSupportActionBar().setTitle("Fehler!");
                break;
            }
        }
    }

    private void addScript(){
        this.getSupportActionBar().setTitle("Skript erstellen");
        mScriptItem = new ScriptItem();
        refreshViews(mScriptItem);
    }

    private void editScript(){
        this.getSupportActionBar().setTitle("Skript editieren");
        mScriptItemString = getIntent().getStringExtra("item");
        try {
            mScriptItem = new ScriptItem(new JSONObject(mScriptItemString));
            refreshViews(mScriptItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshViews(ScriptItem scriptItem){
        mTextViewName.setText(scriptItem.getName());
        mTextViewDescription.setText(scriptItem.getDescription());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_script_edit, menu);
        return true;
    }

    public void saveScriptItem(File scriptFile){
        try {
            if (scriptFile != null) {
                try {
                    String fileType = FileManager.getFileExtension(scriptFile.getName());
                    mScriptItem.setScriptFileType(fileType);
                    File destFile = new File(MainActivity.ScriptDir, mScriptItem.getScriptFile());
                    FileManager.copyFile(scriptFile, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mScriptItemString = mScriptItem.asJsonObject().toString(2);
            Intent intent = new Intent();
            intent.putExtra("resultItem", mScriptItemString);
            setResult(RESULT_OK, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel_script) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.action_save_script){
            saveScriptItem(mImportedScriptFile);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == IMPORT_SCRIPT_REQUEST){
            if (resultCode == RESULT_OK) {
                String curFileName = data.getStringExtra("GetFileName");
                mScriptItem.setName(curFileName);
                mTextViewName.setText(curFileName);
                mImportedScriptFile = new File(data.getStringExtra("GetPath"), curFileName);
            }
        }
    }
}
