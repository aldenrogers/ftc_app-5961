package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.HashSet;
import java.util.Set;

public class TankDrive {
    private Set<DcMotor> leftMotors;
    private Set<DcMotor> rightMotors;

    public TankDrive() {
        leftMotors = new HashSet<DcMotor>();
        rightMotors = new HashSet<DcMotor>();
    }

    public void addLeftMotor(DcMotor m) {
        leftMotors.add(m);
    }

    public void addRightMotor(DcMotor m) {
        rightMotors.add(m);
    }

    public void setPower(double left, double right) {
        for (DcMotor l : leftMotors) {
            l.setPower(left);
        }
        for (DcMotor r : rightMotors) {
            r.setPower(right);
        }
    }
}
