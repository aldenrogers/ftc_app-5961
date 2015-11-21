package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.AnalogInput;

public class TouchSensor {
    private AnalogInput sensor;

    public TouchSensor(AnalogInput sensor) {
        this.sensor = sensor;
    }

    public boolean isPressed() {
        return sensor.getValue() > 512;
    }
}
