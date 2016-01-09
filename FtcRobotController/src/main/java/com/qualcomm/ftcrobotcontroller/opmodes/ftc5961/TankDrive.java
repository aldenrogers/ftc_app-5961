package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDrive {
    private DcMotorSet leftMotors;
    private DcMotorSet rightMotors;
    private int currentId;
    private double pidWindow;
    private double Kp;
    private double Ki;
    private double Kd;
    private double errorAccumulator;
    private double lastError;

    public TankDrive() {
        leftMotors = new DcMotorSet();
        rightMotors = new DcMotorSet();

        pidWindow = 30;
        Kp = 0.1;
        Ki = 0.002;
        Kd = 1;
        // It's not a problem that the first directed drive maneuver might have
        // id 0, because 0 is the value to which errorAccumulator is reset upon
        // a change in id.
        currentId = 0;
        errorAccumulator = 0;
        lastError = Double.NaN;
    }

    public void addLeftMotor(DcMotor m) {
        leftMotors.add(m);
    }

    public void addRightMotor(DcMotor m) {
        rightMotors.add(m);
    }

    public void setPower(double left, double right) {
        leftMotors.setPower(left);
        rightMotors.setPower(right);
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
     * heading. To use proportional-integral-derivative control, some data must
     * be saved across repeated invocations. Passing a new {@code id} indicates
     * that data should be discarded because the target heading has changed.
     * @param power the maximum power to assign to one side or the other. Use 0
     *              to request a point turn towards the target heading.
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
        double derivative;
        if (!Double.isNaN(lastError)) {
            derivative = error - lastError;
        } else {
            derivative = 0;
        }
        lastError = error;
        if (Math.abs(error) > pidWindow) {
            // The error is so big that PID control would be counterproductive,
            // because the integral component would reduce stability, while the
            // derivative component would act against the proportional one even
            // though there is no risk of overshooting the distant target. Note
            // that this resets the integral for the future, too.
            errorAccumulator = 0;
            derivative = 0;
        }
        double u = Kp * error + Ki * errorAccumulator + Kd * derivative;
        if (power != 0) {
            if (u > 0) {
                setPowerQuiet((1 - u) * power, power);
            } else {
                setPowerQuiet(power, (1 + u) * power);
            }
        } else {
            setPowerQuiet(-u, u);
        }
    }

    public int sumLeftEncoders() {
        return leftMotors.sumEncoders();
    }

    public int sumRightEncoders() {
        return rightMotors.sumEncoders();
    }

    public int sumEncoders() {
        return sumLeftEncoders() + sumRightEncoders();
    }
}
