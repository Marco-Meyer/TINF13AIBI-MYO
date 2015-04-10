package de.vuzixcontrol;

/**
 * Created by Alexander on 10.04.2015.
 */

public class Constants {
    public enum ACTIONS 
    {
        public static final int DIALOGUE = 0;           //Preferences have to be send seperated
        public static final int TOAST  = 1;             //Text has to be send seperated
        public static final int KEY = 2;                //Key has to be send seperated -> ASCII-Code is used
        public static final int START_ACTIVITY = 3;     //Activity has to be send seperated

        public enum SWIPE
        {
            UP, DOWM, LEFT, RIGHT
        }

        public enum TAP
        {
            SINGLE, DOUBLE, HOLD
        }

        public enum KEY_VUZIX
        {
            UP, DOWN, OK
        }
    }
}