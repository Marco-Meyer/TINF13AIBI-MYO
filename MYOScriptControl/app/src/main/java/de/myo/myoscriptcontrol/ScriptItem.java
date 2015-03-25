package de.myo.myoscriptcontrol;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Daniel on 18.03.2015.
 */
public class ScriptItem {
    private UUID mId;
    private String mName;
    private String mDate;
    private String mScriptFileType;
    private String mDescription;

    public void insertJsonData(JSONObject json){
        mId = UUID.fromString(json.optString("id"));
        mName = json.optString("name");
        mDate = json.optString("date");
        mScriptFileType = json.optString("scriptFile");
        mDescription = json.optString("description");
    }

    public ScriptItem(){
        mId = UUID.randomUUID();
        mName = "Unbenannt";
        mScriptFileType = "";
        mDescription = "";
        mDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale.getDefault()).format(new Date());
    }

    public ScriptItem(JSONObject json){
        insertJsonData(json);
    }

    public JSONObject asJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mId.toString());
            json.put("name", mName);
            json.put("scriptFile", mScriptFileType);
            json.put("date", mDate);
            json.put("description", mDescription);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public UUID getId() {
        return mId;
    }

    public String getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getScriptFile() {
        return mId.toString() + "." + mScriptFileType;
    }

    public String getScriptFileType() {
        return  mScriptFileType;
    }

    public void setScriptFileType(String fileType) {
        mScriptFileType = fileType;
    }
}
