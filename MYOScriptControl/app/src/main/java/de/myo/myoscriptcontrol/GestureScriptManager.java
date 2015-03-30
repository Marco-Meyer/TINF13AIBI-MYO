package de.myo.myoscriptcontrol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Daniel on 18.03.2015.
 */
public class GestureScriptManager {
    static private GestureScriptManager instance;
    private ArrayList<GestureItem> mGestureList;
    private ArrayList<ScriptItem> mScriptList;
    private File mConfigFile;

    private GestureScriptManager(){
        mGestureList = new ArrayList<>();
        mScriptList = new ArrayList<>();
    }

    static public GestureScriptManager getInstance() {
        if (instance == null) {
            instance = new GestureScriptManager();
        }
        return instance;
    }

    public ScriptItem getScriptByUUID(UUID uuid){
        for (ScriptItem item : mScriptList){
            if (item.getId().compareTo(uuid)==0){
                return item;
            }
        }
        return null;
    }

    private void loadGestureListFromJson(JSONArray json){
        int count = json.length();
        for (int i=0; i<count; i++){
            mGestureList.add(new GestureItem(json.optJSONObject(i)));
        }
    }

    private void loadScriptListFromJson(JSONArray json){
        int count = json.length();
        for (int i=0; i<count; i++){
            mScriptList.add(new ScriptItem(json.optJSONObject(i)));
        }
    }

    public void loadFromJsonFile(File file) throws JSONException, IOException {
        if (file.exists()){
            String jsonFileString = FileManager.getStringFromFile(file.getAbsolutePath());
            JSONObject jsonObject = new JSONObject(jsonFileString);
            JSONArray jsonGestureArray = jsonObject.optJSONArray("gestures");
            JSONArray jsonScriptArray = jsonObject.optJSONArray("scripts");
            loadGestureListFromJson(jsonGestureArray);
            loadScriptListFromJson(jsonScriptArray);
        }
    }

    private JSONObject asJsonObject() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonGestures = new JSONArray();
        JSONArray jsonScripts = new JSONArray();
        for (GestureItem item : mGestureList){
            jsonGestures.put(item.asJsonObject());
        }
        for (ScriptItem item : mScriptList){
            jsonScripts.put(item.asJsonObject());
        }
        jsonObject.put("gestures", jsonGestures);
        jsonObject.put("scripts", jsonScripts);

        return jsonObject;
    }

    public void saveToJsonFile(File file) throws JSONException, IOException{
        JSONObject jsonObject = asJsonObject();
        String test = jsonObject.toString(2);
        FileOutputStream fOut = new FileOutputStream(file);
        fOut.write(test.getBytes());
    }

    public void saveToJsonFile() throws IOException, JSONException {
        saveToJsonFile(mConfigFile);
    }

    public ArrayList<GestureItem> getGestureList() {
        return mGestureList;
    }

    public void setGestureList(ArrayList<GestureItem> mGestureList) {
        this.mGestureList = mGestureList;
    }

    public ArrayList<ScriptItem> getScriptList() {
        return mScriptList;
    }

    public void setScriptList(ArrayList<ScriptItem> mScriptList) {
        this.mScriptList = mScriptList;
    }

    public File getConfigFile() {
        return mConfigFile;
    }

    public void setConfigFile(File configFile) throws IOException, JSONException {
        this.mConfigFile = configFile;
        loadFromJsonFile(mConfigFile);
    }
}
