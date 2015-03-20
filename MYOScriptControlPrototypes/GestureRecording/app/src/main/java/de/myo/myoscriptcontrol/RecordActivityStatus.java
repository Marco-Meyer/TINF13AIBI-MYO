package de.myo.myoscriptcontrol;

/**
 * Created by Tommy on 15.03.2015.
 */
public enum RecordActivityStatus {
    UNKNOWN("unknown"),
    DISCONNECTED("disconnected"),
    DETACHED("detached"),
    UNSYNCED("unsynced"),
    LOCKED("locked"),
    UNCENTERED("locked"),
    RECORDING("recording"),
    PLAYING("playing"),
    SEARCHING_MYO("Searcing MYO"),
//    ESCAPED("escaped"),
    IDLE("idle");

    private String mStringValue;

    RecordActivityStatus(String value){
        mStringValue = value;
    }

    @Override
    public String toString() {
        return mStringValue;
    }

    public String getValue() {
        return mStringValue;
    }

    public static RecordActivityStatus parseString(String value) {
        for(RecordActivityStatus v : values())
            if(v.getValue().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
