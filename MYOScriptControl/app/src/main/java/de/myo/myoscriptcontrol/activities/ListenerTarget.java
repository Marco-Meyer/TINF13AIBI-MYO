package de.myo.myoscriptcontrol.activities;

import com.thalmic.myo.Pose;

import de.myo.myoscriptcontrol.gesturerecording.GridPosition;
import de.myo.myoscriptcontrol.gesturerecording.MYOStatus;

/**
 * Created by felix on 21.03.2015.
 */
public interface ListenerTarget {
    void OnPose(Pose pose);
    void OnGridPositionUpdate(GridPosition position);
    void OnUpdateStatus(MYOStatus status);
}
