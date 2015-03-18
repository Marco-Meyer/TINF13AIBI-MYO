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

/**
 * Created by Tommy on 18.03.2015.
 */
public class GestureScriptManager {
    private ArrayList<GestureItem> mGestureList;
    private ArrayList<ScriptItem> mScriptList;
    private File mConfigFile;

    public GestureScriptManager(){
        mGestureList = new ArrayList<>();
        mScriptList = new ArrayList<>();
    }

    public GestureScriptManager(File file){
        this();
        mConfigFile = file;
        loadFromJsonFile(mConfigFile);
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    private String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
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

    public void loadFromJsonFile(File file){
        if (file.exists()){
            try {
                String jsonFileString = getStringFromFile(file.getAbsolutePath());
                JSONObject jsonObject = new JSONObject(jsonFileString);
                JSONArray jsonGestureArray = jsonObject.optJSONArray("gestures");
                JSONArray jsonScriptArray = jsonObject.optJSONArray("scripts");
                loadGestureListFromJson(jsonGestureArray);
                loadScriptListFromJson(jsonScriptArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject asJsonObject(){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonGestures = new JSONArray();
        JSONArray jsonScripts = new JSONArray();
        for (GestureItem item : mGestureList){
            jsonGestures.put(item.asJsonObject());
        }
        for (ScriptItem item : mScriptList){
            jsonScripts.put(item.asJsonObject());
        }
        try {
            jsonObject.put("gestures", jsonGestures);
            jsonObject.put("scripts", jsonScripts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void saveToJsonFile(File file){
        JSONObject jsonObject = asJsonObject();
        try {
            String test = jsonObject.toString(2);
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(test.getBytes());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public void saveToJsonFile(){
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

    public void setConfigFile(File mConfigFile) {
        this.mConfigFile = mConfigFile;
    }
}
