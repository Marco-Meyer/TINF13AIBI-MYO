package de.myo.myoscriptcontrol;

import android.graphics.Point;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

/**
 * Created by Tommy on 15.03.2015.
 */

public class GestureRecordDeviceListener extends AbstractDeviceListener {
    private boolean mSynced, mAttached, mConnected, mUnlocked, mRecording;
    private Quaternion mOrientation;
    private Pose mPose;
    private Point mArmPosition, mLastSetPoint;

    private double PI = Math.PI;    // 180°
    private double TWOPI = PI*2;    // 360°
    private double EIGHTHPI = PI/8; // 22.5°

    private double centreYaw = 0.0;
    private double centrePitch = 0.0;

    private double YAW_DEADZONE = EIGHTHPI;
    private double PITCH_DEADZONE = EIGHTHPI;

    private GesturePattern mTempPattern;
    private Myo mMyo;

    public GestureRecordDeviceListener(){
        mArmPosition = new Point(2, 2);
        mLastSetPoint = new Point(2, 2);
    }

    public RecordActivityStatus getStatus() {
        if (mConnected) {
            if (mAttached) {
                if (mSynced) {
                    if (mUnlocked) {
                        if (mRecording) {
                            return RecordActivityStatus.RECORDING;
                        } else {
                            return RecordActivityStatus.IDLE;
                        }
                    } else {
                        return RecordActivityStatus.LOCKED;
                    }
                } else {
                    return RecordActivityStatus.UNSYNCED;
                }
            } else {
                return RecordActivityStatus.DETACHED;
            }
        } else {
            return RecordActivityStatus.DISCONNECTED;
        }
    }

    private double calculateDeltaRadians(double current, double centre){
        double delta = current - centre;
        if (delta > PI) {
            delta = delta - TWOPI;
        } else if (delta < -PI) {
            delta = delta + TWOPI;
        }
        return delta;
    }

    private void onPeriodic(Quaternion orientation){
        if (centreYaw == 0) {
            return;
        }
        double currentYaw = Quaternion.yaw(orientation);
        double currentPitch = Quaternion.pitch(orientation);
        double deltaYaw = calculateDeltaRadians(currentYaw, centreYaw);
        double deltaPitch = calculateDeltaRadians(currentPitch, centrePitch);

        int x,y;
        if (deltaYaw < -YAW_DEADZONE){
            x = 1;
        } else if (deltaYaw > YAW_DEADZONE){
            x=-1;
        } else {
            x=0;
        }

        XDirection direction = mMyo.getXDirection();
        switch (direction){
            case TOWARD_WRIST:{
                if (deltaPitch < -PITCH_DEADZONE){
                    y=1;
                } else if (deltaPitch > PITCH_DEADZONE){
                    y=-1;
                } else {
                    y=0;
                }
                break;
            }
            default: {
                if (deltaPitch < -PITCH_DEADZONE) {
                    y = -1;
                } else if (deltaPitch > PITCH_DEADZONE) {
                    y = 1;
                } else {
                    y = 0;
                }
                break;
            }
        }
        mArmPosition.set(x, y);
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

    private void centre(){
        mArmPosition.set(0, 0);
        mLastSetPoint.set(2, 2);
        if (mOrientation!=null) {
            centreYaw = Quaternion.yaw(mOrientation);
            centrePitch = Quaternion.pitch(mOrientation);
            mTempPattern = new GesturePattern();
//            doLogEntry("zentriert");
        }
    }

    private void escape(){
//        doLogEntry("escaped");
        mMyo.lock();
        centreYaw = 0.0;
        centrePitch = 0.0;
        mArmPosition.set(2, 2);
        mLastSetPoint.set(2, 2);
    }

    private void onPoseMain(Pose pose){
//        doLogEntry(pose.name());
        if (pose == Pose.FIST) {
            mLastSetPoint.set(mArmPosition.x, mArmPosition.y);
            mTempPattern.add(pointToGridPosition(mLastSetPoint));
//            doLogEntry(mTempPattern.asJsonArray().toString());
        } else if (pose == Pose.FINGERS_SPREAD) {
            escape();
            //TODO save pattern
//            doLogEntry(mTempPattern.asJsonArray().toString());
        } else if (pose == Pose.WAVE_OUT) {
            escape();
            mTempPattern.clear();
            //TODO delete pattern
//            doLogEntry(mTempPattern.asJsonArray().toString());
        }
    }

    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        super.onArmSync(myo, timestamp, arm, xDirection);
        mConnected = true;
        mSynced = true;
        mMyo = myo;
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
        super.onArmUnsync(myo, timestamp);
        mConnected = true;
        mSynced = false;
        mMyo = myo;
    }

    @Override
    public void onConnect(Myo myo, long timestamp) {
//        doLogEntry("Myo Connected!");
        mConnected = true;
        mMyo = myo;
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
//        doLogEntry("Myo Disconnected!");
        mConnected = false;
        mMyo = myo;
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        if (mRecording) {
            mPose = pose;
            onPoseMain(pose);
        }
        mConnected = true;
        mSynced = true;
        mMyo = myo;
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
        super.onUnlock(myo, timestamp);
        mUnlocked = true;
        mSynced = true;
        mConnected = true;
//        doLogEntry("Unlock");
        if (mRecording) {
            myo.unlock(Myo.UnlockType.HOLD);
            centre();
        }
        mMyo = myo;
    }

    @Override
    public void onDetach(Myo myo, long timestamp) {
        super.onDetach(myo, timestamp);
        mAttached = false;
        mConnected = true;
//        doLogEntry("Detach");
        mMyo = myo;
    }

    @Override
    public void onAttach(Myo myo, long timestamp) {
        super.onAttach(myo, timestamp);
        mAttached = true;
        mConnected = true;
//        doLogEntry("Attach");
        mMyo = myo;
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
        super.onLock(myo, timestamp);
        mUnlocked = false;
        mConnected = true;
//        doLogEntry("Lock");
        mMyo = myo;
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
        super.onGyroscopeData(myo, timestamp, gyro);
        mMyo = myo;
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
        super.onAccelerometerData(myo, timestamp, accel);
        mMyo = myo;
    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
        super.onOrientationData(myo, timestamp, rotation);
        mOrientation = rotation;
        onPeriodic(rotation);
    }

    public boolean isRecording() {
        return mRecording;
    }

    public void setRecording(boolean mRecording) {
        this.mRecording = mRecording;
    }

    public Point getArmPosition() {
        return mArmPosition;
    }

    public void setArmPosition(Point mArmPosition) {
        this.mArmPosition = mArmPosition;
    }

    public Point getLastSetPoint() {
        return mLastSetPoint;
    }

    public void setLastSetPoint(Point mLastSetPoint) {
        this.mLastSetPoint = mLastSetPoint;
    }

    public GesturePattern getTempPattern() {
        return mTempPattern;
    }

    public void setTempPattern(GesturePattern mTempPattern) {
        this.mTempPattern = mTempPattern;
    }

    public Pose getPose() {
        return mPose;
    }
}
