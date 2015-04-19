package de.myo.vuzixcontrol;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public class CommandExecutionService extends IntentService {

    public static final String ACTION_INJECT_KEYEVENT = "de.myo.vuzixcontrol.action.execcommand";
    public static final String ACTION_INJECT_LONGPRESS = "de.myo.vuzixcontrol.action.longpress";
    public static final String ACTION_PERFORM_BACKPRESSED = "de.myo.vuzixcontrol.action.backpressed";
    public static final String EVENT_CODE = "de.myo.vuzixcontrol.extra.eventcode";

    public static void startWithAction(Context context, String action, int eventCode) {
        Intent intent = new Intent(context, CommandExecutionService.class);
        intent.setAction(action);
        intent.putExtra(EVENT_CODE, eventCode);
        context.startService(intent);
    }

    public CommandExecutionService() {
        super("CommandExecutionService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INJECT_KEYEVENT.equals(action)) {
                injectKeyEvent(intent.getIntExtra(EVENT_CODE, 0));
            }
            if(ACTION_PERFORM_BACKPRESSED.equals(action)) {
                performBackPressed();
            }
        }
    }

    private void injectKeyEvent(int eventCode) {
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.sendKeyDownUpSync(eventCode);
    }

    private void performBackPressed() {
        sendBroadcast(new Intent("CloseActivity"));
    }

    private void injectLongPressEvent(int eventCode) {
        try {
            Instrumentation instrumentation = new Instrumentation();
            instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, eventCode));
            Thread.sleep(ViewConfiguration.get(this.getApplicationContext()).getLongPressTimeout() + 10);
            instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, eventCode));
        } catch (Exception e) {
            Log.e("Error on Longpress", e.getMessage());
        }

    }
}
