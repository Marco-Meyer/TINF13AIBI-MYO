package de.myo.myoscriptcontrol;

import android.content.Context;
import android.graphics.Point;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Tommy on 15.03.2015.
 */

public class GestureRecordDeviceListener extends AbstractDeviceListener {
    private boolean mSynced, mConnected, mUnlocked, mAttached;
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
    private ArrayList<ListenerTarget> mTargets;


    private void notifyPose(Pose pose) {
        for(ListenerTarget target : mTargets) {
            target.OnPose(pose);
        }
    }

    private void notifyGridPos(GridPosition position) {
        for(ListenerTarget target : mTargets) {
            target.OnGridPositionUpdate(position);
        }
    }

    private void notifyUpdateStatus(String status) {
        for(ListenerTarget target : mTargets) {
            target.OnUpdateStatus(status);
        }
    }


    public GestureRecordDeviceListener(){
        mArmPosition = new Point(2, 2);
        mLastSetPoint = new Point(2, 2);
        mTargets = new ArrayList<ListenerTarget>();
    }


    public void addTarget(ListenerTarget target) {
        mTargets.add(target);
    }

    public void removeTarget(ListenerTarget target) {
        mTargets.remove(target);
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        super.onPose(myo, timestamp, pose);
        if(pose == Pose.DOUBLE_TAP) {
            centre();
        }
        mMyo = myo;
        notifyPose(pose);
    }


    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        super.onArmSync(myo, timestamp, arm, xDirection);
        mConnected = true;
        mSynced = true;
        mMyo = myo;
        notifyUpdateStatus("LOCKED");
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
        super.onArmUnsync(myo, timestamp);
        mConnected = true;
        mSynced = false;
        mMyo = myo;
        notifyUpdateStatus("UNSYNCED");
    }

    @Override
    public void onConnect(Myo myo, long timestamp) {
        super.onConnect(myo, timestamp);
        mConnected = true;
        mMyo = myo;
        notifyUpdateStatus("UNSYNCED");
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
        super.onDisconnect(myo, timestamp);
        mConnected = false;
        mMyo = myo;
        notifyUpdateStatus("DISCONNECTED");
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
        super.onUnlock(myo, timestamp);
        mUnlocked = true;
        myo.unlock(Myo.UnlockType.HOLD);
        centre();
        mMyo = myo;
        notifyUpdateStatus("IDLE");
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
        super.onLock(myo, timestamp);
        mUnlocked = false;
        mConnected = true;
        mMyo = myo;
        notifyUpdateStatus("LOCKED");
    }

    @Override
    public void onDetach(Myo myo, long timestamp) {
        super.onDetach(myo, timestamp);
        mAttached = false;
        mConnected = true;
        mMyo = myo;
    }

    @Override
    public void onAttach(Myo myo, long timestamp) {
        super.onAttach(myo, timestamp);
        mAttached = true;
        mConnected = true;
        mMyo = myo;
        notifyUpdateStatus("UNSYNCED");
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
        notifyGridPos(pointToGridPosition(mArmPosition));
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

    private double calculateDeltaRadians(double current, double centre){
        double delta = current - centre;
        if (delta > PI) {
            delta = delta - TWOPI;
        } else if (delta < -PI) {
            delta = delta + TWOPI;
        }
        return delta;
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
        }
    }

    private void escape(){
        mMyo.lock();
        centreYaw = 0.0;
        centrePitch = 0.0;
        mArmPosition.set(2, 2);
        mLastSetPoint.set(2, 2);
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
}
