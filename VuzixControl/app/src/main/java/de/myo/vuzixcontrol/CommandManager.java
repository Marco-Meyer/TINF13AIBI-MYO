package de.myo.vuzixcontrol;

import java.util.HashMap;

/**
 * Created by felix on 17.04.2015.
 */
public class CommandManager {
    public static String MOVE_LEFT = "MOVELEFT";
    public static String MOVE_RIGHT = "MOVERIGHT";
    public static String GO_BACK = "GOBACK";
    public static String SELECT = "SELECT";

    private static HashMap<String, Integer> commands = initCommands();

    public static Integer getCommandforString(String inputString) {
        if(inputString.startsWith(MOVE_LEFT)) {
            return commands.get(MOVE_LEFT);
        }
        if(inputString.startsWith(MOVE_RIGHT)) {
            return commands.get(MOVE_RIGHT);
        }
        if(inputString.startsWith(SELECT)) {
            return commands.get(SELECT);
        }
        if(inputString.startsWith(GO_BACK)) {
            return commands.get(GO_BACK);
        }
        return null;
    }

    private static HashMap<String,Integer> initCommands() {
        HashMap<String, Integer> commands = new HashMap<>();
        commands.put(MOVE_LEFT, 21);
        commands.put(MOVE_RIGHT, 22);
        commands.put(SELECT, 66);
        commands.put(GO_BACK, 4);
        return commands;
    }
}
