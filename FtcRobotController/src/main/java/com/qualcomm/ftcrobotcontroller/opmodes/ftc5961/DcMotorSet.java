package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.HashSet;

/**
 * Provides convenience methods for using several synchronized motors.
 */
public class DcMotorSet extends HashSet<DcMotor> {
    public void setPower(double power) {
        for (DcMotor m : this) {
            m.setPower(power);
        }
    }

    public int sumEncoders() {
        int result = 0;
        for (DcMotor m : this) {
            result += m.getCurrentPosition();
        }
        return result;
    }
}
