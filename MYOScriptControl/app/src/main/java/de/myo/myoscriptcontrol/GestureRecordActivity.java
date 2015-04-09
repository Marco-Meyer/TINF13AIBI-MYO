package de.myo.myoscriptcontrol;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import de.myo.myoscriptcontrol.gesturerecording.*;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;

public class GestureRecordActivity extends ActionBarActivity implements ListenerTarget {

    private GesturePattern mPattern;
    private String mPatternString;
    private Pose mPose;
    private static GestureRecordDeviceListener mMyoListener;
    private HashMap<GridPosition, ImageView> mPositionImageMap;
    private RecordActivityStatus mStatus = RecordActivityStatus.UNKNOWN;
    private GridPosition mCurrentPosition;
    private GridPosition mLastPosition;
    private boolean mRecording;

    @Override
    public void OnPose(Pose pose) {
        OnUpdateStatus("IDLE");
        mPose = pose;
        if(mRecording) {
            if(mPose == Pose.FIST) {
                mPattern.add(mCurrentPosition);
                showPattern();
                showSetGridPosition();
                clearLastPosition();
                mLastPosition = mCurrentPosition;
            }
            if(mPose == Pose.FINGERS_SPREAD) {
                findViewById(R.id.imageButtonRecordPattern).callOnClick();
            }
            if (mPose == Pose.WAVE_OUT) {
                mPattern.clear();
                showPattern();
            }
            if(mPose == Pose.REST) {
                showLastPositionOnGrid();
            }
        }
    }

    @Override
    public void OnUpdateStatus(String status) {
        mStatus = RecordActivityStatus.valueOf(status);
        updateStatusText();
    }

    @Override
    public void OnGridPositionUpdate(GridPosition position) {
        if(mRecording) {
            mCurrentPosition = position;
            showPositionOnGrid();
        }
    }

    private void showPattern(){
        if (mPattern!=null) {
            GridView grid = (GridView)findViewById(R.id.gridViewRecordedPattern);
            GesturePatternGridViewAdapter gridAdapter = new GesturePatternGridViewAdapter(this, mPattern);
            grid.setAdapter(gridAdapter);
        }
    }

    private void updateStatusText(){
        ((TextView)findViewById(R.id.textViewRecordStatus)).setText(mStatus.toString());
    }

    private void showLastPositionOnGrid(){
        ImageView image = mPositionImageMap.get(mLastPosition);
        if (image != null){
            image.setBackgroundColor(Color.LTGRAY);
        }
    }

    private void clearLastPosition() {
        ImageView image = mPositionImageMap.get(mLastPosition);
        if (image != null){
            image.setBackgroundColor(getResources().getColor(R.color.activity_background));
        }
    }

    private void showSetGridPosition() {
        ImageView image = mPositionImageMap.get(mCurrentPosition);
        image.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
    }

    private void showPositionOnGrid(){
        clearGridPosition();
        ImageView image = mPositionImageMap.get(mCurrentPosition);
        if (image != null) {
            image.setImageResource(R.drawable.ic_arm_pos_on);
        }
    }

    private void clearGridPosition(){
        for (ImageView image : mPositionImageMap.values()){
            if (image != null) {
                image.setImageResource(R.drawable.ic_arm_pos_off);
            }
        }
    }

    private void initializeButtonListeners(){
        mRecording = false;
        final ImageButton buttonRecord = (ImageButton)findViewById(R.id.imageButtonRecordPattern);
        final ImageButton buttonDelete = (ImageButton)findViewById(R.id.imageButtonDeletePattern);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecording) {
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_record));
                    v.setBackgroundColor(getResources().getColor(R.color.myosdk__thalmic_blue));
                    clearGridPosition();
                    clearLastPosition();
                }
                else {
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_av_stop));
                    v.setBackgroundColor(getResources().getColor(R.color.myosdk__indicator_green));
                    showPositionOnGrid();
                }
                mRecording = !mRecording;
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPattern.clear();
                showPattern();
            }
        });
    }

    private void initMyoHub(){
        mMyoListener = GestureRecordDeviceListener.getInstance();
        mMyoListener.addTarget(this);
        OnUpdateStatus(mMyoListener.getStatus());
    }



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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentPosition = GridPosition.POS_CENTER;
        mPose = Pose.UNKNOWN;
        initializeIntentVars(getIntent());
        initializeGridImageMap();
        initializeButtonListeners();
        initMyoHub();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyoListener.removeTarget(this);
        mRecording = false;
        clearGridPosition();
    }

    void initializeIntentVars(Intent intent){
        mPatternString = intent.getStringExtra("pattern");
        try {
            mPattern = new GesturePattern(new JSONArray(mPatternString));
            showPattern();
        } catch (JSONException e) {
            e.printStackTrace();
            ErrorActivity.handleError(this, e.getMessage());
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
                ErrorActivity.handleError(this, e.getMessage());
            }
        } else if (id == R.id.action_cancel_pattern) {
            setResult(RESULT_CANCELED);
            finish();
        }
        mPattern.clear();
        return super.onOptionsItemSelected(item);
    }

    public GesturePattern getPattern() {
        return mPattern;
    }

    public void setRecording(boolean mRecording) {
        this.mRecording = mRecording;
    }
}
