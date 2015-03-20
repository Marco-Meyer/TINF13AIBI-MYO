package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.Hub;
import com.thalmic.myo.Pose;
import com.thalmic.myo.scanner.ScanActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class GestureRecordActivity extends ActionBarActivity{

    private GesturePattern mPattern;
    private String mPatternString;
    private Hub mMyoHub;
    private GestureRecordDeviceListener mMyoListener;
    private Handler mHandler;
    private TextView mTextLog;
    private HashMap<GridPosition, ImageView> mPositionImageMap;
    private boolean mRecording = false;
    private boolean mPlaying = false;
    private RecordActivityStatus mStatus = RecordActivityStatus.UNKNOWN;
//    private int count = 0;

    private void useHandler() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 20);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateStatus();
            updateRecordAndPlayButton();
            if (mStatus!=RecordActivityStatus.UNKNOWN &&
                mStatus != RecordActivityStatus.DISCONNECTED &&
                mStatus != RecordActivityStatus.PLAYING) {
                    mRecording = mMyoListener.isRecording();
                    if (mRecording) {
                        updateMyoPosition();
                        showPattern();
                    }
            }
            mHandler.postDelayed(mRunnable, 20);
        }
    };

    private void showPattern(){
        GesturePattern pattern = mMyoListener.getTempPattern();
        if (pattern!=null) {
            mTextLog.setText(pattern.asJsonArray().toString());
//            count++;
//            if (count>100) {
//                Toast.makeText(getApplicationContext(), pattern.asJsonArray().toString(), Toast.LENGTH_SHORT).show();
//                count=0;
//            }
        }
    }

    private void updateStatus(){
        if (mPlaying){
            mStatus = RecordActivityStatus.PLAYING;
        } else {
            mStatus = mMyoListener.getStatus();
        }
        TextView textView = (TextView)findViewById(R.id.textViewRecordStatus);
        textView.setText(mStatus.toString());
    }

    private void updateRecordAndPlayButton(){
        ImageButton buttonRecord = (ImageButton)findViewById(R.id.imageButtonRecordPattern);
        ImageButton buttonPlay = (ImageButton)findViewById(R.id.imageButtonPlayRecordedPattern);
        if (mRecording){
            buttonRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_av_stop));
            buttonPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_av_play));
            buttonRecord.setBackgroundColor(getResources().getColor(R.color.myosdk__indicator_green));
            buttonPlay.setBackgroundColor(Color.LTGRAY);
            buttonPlay.setEnabled(false);
        } else if (mPlaying) {
            buttonRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_record));
            buttonPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_av_stop));
            buttonRecord.setBackgroundColor(Color.LTGRAY);
            buttonPlay.setBackgroundColor(getResources().getColor(R.color.myosdk__indicator_green));
            buttonRecord.setEnabled(false);
        } else {
            buttonRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_record));
            buttonPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_av_play));
            buttonRecord.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
            buttonPlay.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
            buttonRecord.setEnabled(true);
            buttonPlay.setEnabled(true);
        }
    }


    private void updateMyoPosition(){
        clearGridPosition();
        showLastPositionOnGrid();
        showPositionOnGrid();
    }

    public GridPosition pointToGridPosition(Point position){
        if (position.x==0 && position.y == 0){
            return GridPosition.POS_CENTER;
        }
        if (position.x==-1 && position.y == 0){
            return GridPosition.POS_WEST;
        }
        if (position.x==1 && position.y == 0){
            return GridPosition.POS_EAST;
        }
        if (position.x==0 && position.y == 1){
            return GridPosition.POS_NORTH;
        }
        if (position.x==-1 && position.y == 1){
            return GridPosition.POS_NORTH_WEST;
        }
        if (position.x==1 && position.y == 1){
            return GridPosition.POS_NORTH_EAST;
        }
        if (position.x==0 && position.y == -1){
            return GridPosition.POS_SOUTH;
        }
        if (position.x==-1 && position.y == -1){
            return GridPosition.POS_SOUTH_WEST;
        }
        if (position.x==1 && position.y == -1){
            return GridPosition.POS_SOUTH_EAST;
        } else {
            return GridPosition.POS_UNKNOWN;
        }
    }

    private void clearGridPosition(){
        for (ImageView image : mPositionImageMap.values()){
            if (image != null) {
                image.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_000);
                image.setBackgroundColor(Color.WHITE);
            }
        }
    }

    private void showLastPositionOnGrid(){
        GridPosition posLast = pointToGridPosition(mMyoListener.getLastSetPoint());
        ImageView image = mPositionImageMap.get(posLast);
        if (image != null){
            image.setBackgroundColor(Color.LTGRAY);
        }
    }

    private void showPositionOnGrid(){
        GridPosition position = pointToGridPosition(mMyoListener.getArmPosition());
        ImageView image = mPositionImageMap.get(position);
        if (image != null) {
            image.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
            if (mMyoListener.getPose()==Pose.FIST) {
                image.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
            }
        }
    }

    private void initializeListeners(){
        ImageButton buttonRecord = (ImageButton)findViewById(R.id.imageButtonRecordPattern);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecording){
                    mRecording = false;
                    mPattern = mMyoListener.getTempPattern();
                } else {
                    if (mMyoListener.getStatus()==RecordActivityStatus.DISCONNECTED){
                        Intent intent = new Intent(GestureRecordActivity.this, ScanActivity.class);
                        startActivity(intent);
                    }
                    mRecording = true;
                }
                mMyoListener.setRecording(mRecording);
            }
        });
        ImageButton buttonPlay = (ImageButton)findViewById(R.id.imageButtonPlayRecordedPattern);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaying = !mPlaying;
            }
        });
    }

    private void initMyoHub(){
        MainActivity.removeGestureRecognitionListener();
        mMyoListener = new GestureRecordDeviceListener();
        mMyoHub = MainActivity.getMyoHub();
        mMyoHub.addListener(mMyoListener);
        mMyoHub.setLockingPolicy(Hub.LockingPolicy.STANDARD);
        if (mMyoHub.getConnectedDevices().size()==0){
            mMyoHub.attachToAdjacentMyo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyoHub.removeListener(mMyoListener);
        MainActivity.setGestureRecognitionListener();
    }

//    private void doLogEntry(String message){
//        mTextLog.setText(message + "\n" + mTextLog.getText());
//    }

    private void initializeGridImageMap(){
        mPositionImageMap = new HashMap<>();
        mPositionImageMap.put(GridPosition.POS_NORTH_WEST, (ImageView)findViewById(R.id.imageViewNW));
        mPositionImageMap.put(GridPosition.POS_NORTH, (ImageView)findViewById(R.id.imageViewN));
        mPositionImageMap.put(GridPosition.POS_NORTH_EAST, (ImageView)findViewById(R.id.imageViewNE));
        mPositionImageMap.put(GridPosition.POS_WEST, (ImageView)findViewById(R.id.imageViewW));
        mPositionImageMap.put(GridPosition.POS_CENTER, (ImageView)findViewById(R.id.imageViewC));
        mPositionImageMap.put(GridPosition.POS_EAST, (ImageView)findViewById(R.id.imageViewE));
        mPositionImageMap.put(GridPosition.POS_SOUTH_WEST, (ImageView)findViewById(R.id.imageViewSW));
        mPositionImageMap.put(GridPosition.POS_SOUTH, (ImageView)findViewById(R.id.imageViewS));
        mPositionImageMap.put(GridPosition.POS_SOUTH_EAST, (ImageView)findViewById(R.id.imageViewSE));
        mPositionImageMap.put(GridPosition.POS_UNKNOWN, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_record);
        mTextLog = (TextView)findViewById(R.id.textLogRecord);
        initializeIntentVars(getIntent());
        initializeGridImageMap();
        initializeListeners();
        initMyoHub();
        useHandler();
    }

    void initializeIntentVars(Intent intent){
        mPatternString = intent.getStringExtra("pattern");
        try {
            mPattern = new GesturePattern(new JSONArray(mPatternString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gesture_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_pattern) {
            try {
                mPatternString = mPattern.asJsonArray().toString(2);
                Intent intent = new Intent();
                intent.putExtra("resultPattern", mPatternString);
                setResult(RESULT_OK, intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_cancel_pattern) {
            setResult(RESULT_CANCELED);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
