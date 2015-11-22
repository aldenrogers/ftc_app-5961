package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.HashSet;
import java.util.Set;

public class TankDrive {
    private Set<DcMotor> leftMotors;
    private Set<DcMotor> rightMotors;
    private int currentId;
    private double errorAccumulator;
    private double Kproportional;
    private double Kintegral;

    public TankDrive() {
        leftMotors = new HashSet<DcMotor>();
        rightMotors = new HashSet<DcMotor>();
        // It's not a problem that the first directed drive maneuver might have
        // id 0, because 0 is the value to which errorAccumulator is reset upon
        // a change in id.
        currentId = 0;
        errorAccumulator = 0;
        Kproportional = 0.01;
        Kintegral = 0.001;
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

    public void setPowerQuiet(double left, double right) {
        if (left > 1) {
            left = 1;
        } else if (left < -1) {
            left = -1;
        }
        if (right > 1) {
            right = 1;
        } else if (right < -1) {
            right = -1;
        }
        setPower(left, right);
    }

    /**
     * Drive in a particular direction. The ratio between power on the left and
     * right sides will be such that the robot turns toward the target heading.
     * This method is expected to be called repeatedly to respond to changes in
     * heading. In order to use proportional-integral control, so it saves some
     * data across repeated invocations. Pass a new {@code id} to discard saved
     * data when targeting a different heading.
     * @param power the maximum power to assign to one side or the other
     * @param heading the current heading, in degrees
     * @param target the desired heading, in degrees
     * @param id an identifying number unique to the current drive maneuver
     */
    public void directed(double power, double heading, double target, int id) {
        if (id != currentId) {
            errorAccumulator = 0;
            currentId = id;
        }
        double error = AdafruitIMU.differenceMod(heading, target, 360);
        if (error > 180) {
            error -= 360;
        }
        errorAccumulator += error;
        double u = Kproportional * error + Kintegral * errorAccumulator;
        if (u > 0) {
            setPowerQuiet((1 - u) * power, power);
        } else {
            setPowerQuiet(power, (1 + u) * power);
        }
    }

    public int sumLeftEncoders() {
        int result = 0;
        for (DcMotor l : leftMotors) {
            result += l.getCurrentPosition();
        }
        return result;
    }

    public int sumRightEncoders() {
        int result = 0;
        for (DcMotor r : rightMotors) {
            result += r.getCurrentPosition();
        }
        return result;
    }

    public int sumEncoders() {
        return sumLeftEncoders() + sumRightEncoders();
    }
}
