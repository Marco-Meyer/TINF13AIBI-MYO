package de.myo.myoscriptcontrol.gesturerecording;

/**
 * Created by felix on 24.03.2015.
 */

import android.graphics.Point;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

import java.util.ArrayList;

import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;

public class GestureRecordDeviceListener extends AbstractDeviceListener {
    static private GestureRecordDeviceListener instance;
    private Quaternion mOrientation;
    private Point mArmPosition, mLastSetPoint;

    private double PI = Math.PI;    // 180°
    private double TWOPI = PI*2;    // 360°
    private double EIGHTHPI = PI/8; // 22.5°

    private double centreYaw = 0.0;
    private double centrePitch = 0.0;

    private double YAW_DEADZONE = EIGHTHPI;
    private double PITCH_DEADZONE = EIGHTHPI;

    private Myo mMyo;
    private ArrayList<ListenerTarget> mTargets;

    private RecordActivityStatus mStatus = RecordActivityStatus.DISCONNECTED;

    public RecordActivityStatus getStatus(){
        return mStatus;
    }

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

    private void notifyUpdateStatus(RecordActivityStatus status) {
        mStatus = status;
        for(ListenerTarget target : mTargets) {
            target.OnUpdateStatus(status);
        }
    }


    private GestureRecordDeviceListener(){
        mArmPosition = new Point(2, 2);
        mLastSetPoint = new Point(2, 2);
        mTargets = new ArrayList<>();
    }

    static public GestureRecordDeviceListener getInstance() {
        if (instance == null) {
            instance = new GestureRecordDeviceListener();
        }
        return instance;
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
        }else if ( pose == Pose.FINGERS_SPREAD){
            myo.lock();
        }
        mMyo = myo;
        notifyPose(pose);
    }


    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        super.onArmSync(myo, timestamp, arm, xDirection);
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.LOCKED);
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
        super.onArmUnsync(myo, timestamp);
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.UNSYNCED);
    }

    @Override
    public void onConnect(Myo myo, long timestamp) {
        super.onConnect(myo, timestamp);
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.UNSYNCED);
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
        super.onDisconnect(myo, timestamp);
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.DISCONNECTED);
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
        super.onUnlock(myo, timestamp);
        myo.unlock(Myo.UnlockType.HOLD);
        centre();
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.IDLE);
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
        super.onLock(myo, timestamp);
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.LOCKED);
    }

    @Override
    public void onDetach(Myo myo, long timestamp) {
        super.onDetach(myo, timestamp);
        mMyo = myo;
    }

    @Override
    public void onAttach(Myo myo, long timestamp) {
        super.onAttach(myo, timestamp);
        mMyo = myo;
        notifyUpdateStatus(RecordActivityStatus.UNSYNCED);
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
        calculateArmPosition(rotation);
        notifyGridPos(pointToGridPosition(mArmPosition));
    }

    private int getArmPositionX(double deltaYaw){
        if (deltaYaw < -YAW_DEADZONE){
            return 1;
        } else if (deltaYaw > YAW_DEADZONE){
            return -1;
        } else {
            return 0;
        }
    }

    private int getArmPositionY(double deltaPitch){
        XDirection direction = mMyo.getXDirection();
        int y = (direction == XDirection.TOWARD_WRIST) ? 1 : -1;
        if (deltaPitch < -PITCH_DEADZONE){
            return y;
        } else if (deltaPitch > PITCH_DEADZONE){
            return -y;
        } else {
            return 0;
        }
    }

    private void calculateArmPosition(Quaternion orientation){
        if (centreYaw == 0) {
            return;
        }
        double currentYaw = Quaternion.yaw(orientation);
        double currentPitch = Quaternion.pitch(orientation);
        double deltaYaw = calculateDeltaRadians(currentYaw, centreYaw);
        double deltaPitch = calculateDeltaRadians(currentPitch, centrePitch);

        int x = getArmPositionX(deltaYaw);
        int y = getArmPositionY(deltaPitch);
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
        }
    }

}

