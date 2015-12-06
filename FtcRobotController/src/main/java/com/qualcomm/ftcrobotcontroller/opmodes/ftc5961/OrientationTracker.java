package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Tracks orientation using the phone's onboard sensors
 * @deprecated Use {@link AdafruitIMU} for much more accurate performace.
 */
@Deprecated
public class OrientationTracker implements SensorEventListener {
    private SensorManager phoneSensors;
    private Sensor accelerometer;
    private Sensor magnetic;
    private float[] gravity;
    private float[] geomagnetic;
    private float[] rotation;
    public float[] orientation;

    public OrientationTracker(Context context) {
        phoneSensors = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = phoneSensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic = phoneSensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravity = new float[3];
        geomagnetic = new float[3];
        rotation = new float[9];
        orientation = new float[3];
    }

    public void start() {
        phoneSensors.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        phoneSensors.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop() {
        phoneSensors.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if (SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic)) {
            SensorManager.getOrientation(rotation, orientation);
        }
    }
}
