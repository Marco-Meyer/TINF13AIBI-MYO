package de.myo.myoscriptcontrol;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by DTH on 11.03.2015.
 */

public class GestureItem {
    private UUID mId;
    private String mName;
    private String mScript;
    private GesturePattern mPattern;
    private String mDate;

    public boolean equalPattern(GestureItem item){
        if (item.getPattern().size() != mPattern.size()){
            return false;
        } else {
            int count = mPattern.size();
            for (int i=0; i<count; i++){
                if (!item.getPattern().get(i).equals(mPattern.get(i))){
                    return false;
                }
            }
        }
        return true;
    }

    public void insertJsonData(JSONObject json){
        mId = UUID.fromString(json.optString("id"));
        mName = json.optString("name");
        mScript = json.optString("script");
        mDate = json.optString("date");
        mPattern = new GesturePattern(json.optJSONArray("pattern"));
    }

    public GestureItem(){
        mId = UUID.randomUUID();
        mName = "Unbenannt";
        mScript = "<No Script>";
        mPattern = new GesturePattern();
        mDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale.getDefault()).format(new Date());
    }

    public GestureItem(JSONObject json){
        insertJsonData(json);
    }

    public JSONObject asJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mId.toString());
            json.put("name", mName);
            json.put("script", mScript);
            json.put("date", mDate);
            json.put("pattern", mPattern.asJsonArray());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return json;
    }

    public String getScript() {
        return mScript;
    }

    public void setScript(String mScript) {
        this.mScript = mScript;
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

    public GesturePattern getPattern() {
        return mPattern;
    }

    public void setPattern(GesturePattern mPattern) {
        this.mPattern = mPattern;
    }

    public String getDate() {
        return mDate;
    }

}
