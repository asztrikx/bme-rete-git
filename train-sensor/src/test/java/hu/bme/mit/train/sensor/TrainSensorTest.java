package hu.bme.mit.train.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainUser;

import static org.mockito.Mockito.*;

public class TrainSensorTest {

    private TrainSensorImpl impl;
    private TrainController mockTrainController;
    private TrainUser mockTrainUser;

    @Before
    public void before() {
        mockTrainController = mock(TrainController.class);
        mockTrainUser = mock(TrainUser.class);
        impl = new TrainSensorImpl(mockTrainController, mockTrainUser);
    }

    @Test
    // Setting speedlimit higher than the reference speed. Alarm should not turn on.
    public void settingHigherSpeedLimit() {
        when(mockTrainController.getReferenceSpeed()).thenReturn(155);
        impl.overrideSpeedLimit(180);
        verify(mockTrainUser, times(1)).setAlarmState(false);
    }

    @Test
    // Setting speedlimit lower than the reference speed. Alarm should not turn on.
    public void settingLowerSpeedLimit() {
        when(mockTrainController.getReferenceSpeed()).thenReturn(155);
        impl.overrideSpeedLimit(100);
        verify(mockTrainUser, times(1)).setAlarmState(false);
    }

    @Test
    // Setting speedlimit lower than the reference speed. Alarm *should* turn on.
    public void settingLowerSpeedLimitSetsAlarm() {
        when(mockTrainController.getReferenceSpeed()).thenReturn(155);
        impl.overrideSpeedLimit(50);
        verify(mockTrainUser, times(1)).setAlarmState(true);
    }

    @Test
    // Setting speedlimit lower than 0. Alarm *should* turn on.
    public void settingSpeedLimitBelowZero() {
        // -50 not possible for getReferenceSpeed,but we need to use that to test the operation for speedLimits under zero.
        when(mockTrainController.getReferenceSpeed()).thenReturn(-50);
        impl.overrideSpeedLimit(-10);
        verify(mockTrainUser, times(1)).setAlarmState(true);
    }

    @Test
    // Setting speedlimit higher than 500. Alarm *should* turn on.
    public void settingSpeedLimitOverLimit() {
        when(mockTrainController.getReferenceSpeed()).thenReturn(50);
        impl.overrideSpeedLimit(550);
        verify(mockTrainUser, times(1)).setAlarmState(true);
    }
}
