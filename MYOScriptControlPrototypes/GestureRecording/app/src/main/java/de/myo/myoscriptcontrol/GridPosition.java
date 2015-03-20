package de.myo.myoscriptcontrol;

/**
 * Created by DTH on 11.03.2015.
 */

public enum GridPosition {
    POS_UNKNOWN("X"),
    POS_CENTER("C"),
    POS_NORTH("N"),
    POS_NORTH_EAST("NE"),
    POS_EAST("E"),
    POS_SOUTH_EAST("SE"),
    POS_SOUTH("S"),
    POS_SOUTH_WEST("SW"),
    POS_WEST("W"),
    POS_NORTH_WEST("NW");

    private String mStringValue;

    GridPosition(String value){
        mStringValue = value;
    }

    @Override
    public String toString() {
        return mStringValue;
    }

    public String getValue() {
        return mStringValue;
    }

    public static GridPosition parseString(String value) {
        for(GridPosition v : values())
            if(v.getValue().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }

}
