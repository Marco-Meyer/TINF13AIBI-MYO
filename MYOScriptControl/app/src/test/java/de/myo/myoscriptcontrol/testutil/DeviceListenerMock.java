package de.myo.myoscriptcontrol.testutil;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;

/**
 * Created by felix on 25.03.2015.
 */
public class DeviceListenerMock extends AbstractDeviceListener{
    public DeviceListenerMock() {
        super();
    }

    @Override
    public void onAttach(Myo myo, long timestamp) {
    }

    @Override
    public void onDetach(Myo myo, long timestamp) {

    }

    @Override
    public void onConnect(Myo myo, long timestamp) {

    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {

    }

    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {

    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {

    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {

    }

    @Override
    public void onLock(Myo myo, long timestamp) {

    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {

    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {

    }
}
