package de.myo.myoscriptcontrol;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Tommy on 14.03.2015.
 */

public class GesturePattern extends ArrayList<GridPosition> {
    public GesturePattern(){
        super();
    }

    public GesturePattern(JSONArray json){
        new GesturePattern();
        int count = json.length();
        for (int i=0; i<count; i++){
            String positionString = json.optString(i);
            GridPosition position = GridPosition.parseString(positionString);
            super.add(position);
        }
    }

    public JSONArray asJsonArray(){
        int count = size();
        JSONArray json = new JSONArray();
        for (int i=0; i<count; i++){
            json.put(get(i).toString());
        }
        return json;
    }
}
