package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TestingRobot {
    public final DcMotor leftFront;
    public final DcMotor leftBack;
    public final DcMotor rightFront;
    public final DcMotor rightBack;
    public final TankDrive drive;
    public final DcMotor arm;
    public final Servo bucket;
    public final AdafruitColorSensor leftColor;
    public final AdafruitColorSensor rightColor;
    public final AdafruitIMU imu;
    public final TouchSensor touch;
    private int armOffset;
    private double lastArmPower;
    private static final double ARM_POWER_INCREMENT = 0.02;

    public TestingRobot(HardwareMap map) {
        leftFront = map.dcMotor.get("M1");
        leftBack = map.dcMotor.get("M3");
        rightFront = map.dcMotor.get("M2");
        rightBack = map.dcMotor.get("M4");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        drive = new TankDrive();
        drive.addLeftMotor(leftFront);
        drive.addLeftMotor(leftBack);
        drive.addRightMotor(rightFront);
        drive.addRightMotor(rightBack);

        arm = map.dcMotor.get("M5");
        armOffset = arm.getCurrentPosition();
        lastArmPower = 0;

        bucket = map.servo.get("S1");
        bucket.setPosition(0);

        DeviceInterfaceModule dim = map.deviceInterfaceModule.get("CDI");
        leftColor = new AdafruitColorSensor(dim, 0);

        DeviceInterfaceModule dim2 = map.deviceInterfaceModule.get("CDIExtra");
        rightColor = new AdafruitColorSensor(dim2, 0);
        touch = new TouchSensor(new AnalogInput(dim2, 0));
        imu = new AdafruitIMU(dim2, 1);
    }

    public double getArmPosition() {
        return Math.PI * (960 + (arm.getCurrentPosition() - armOffset)) / 1080;
    }

    /**
     * Set the position of the servo bucket based on the arm motor encoder
     */
    public void levelBucket() {
        double bucketPos = (Math.PI / 2 - getArmPosition()) / Math.PI;
        if (bucketPos < 0) {
            bucketPos = 0;
        } else if (bucketPos > 1) {
            bucketPos = 1;
        }
        bucket.setPosition(bucketPos);
    }

    private double armInPower(double basePower) {
        return 0.01 + basePower * (1 + Math.cos(getArmPosition()));
    }

    private double armOutPower(double basePower) {
        return -0.01 + basePower * (-1 + Math.cos(getArmPosition()));
    }

    public void armIn(double basePower) {
        arm.setPower(armInPower(basePower));
    }

    public void armOut(double basePower) {
        arm.setPower(armOutPower(basePower));
    }

    private boolean armPowerToward(double targetPower) {
        double diff = targetPower - lastArmPower;
        if (Math.abs(diff) < ARM_POWER_INCREMENT) {
            lastArmPower = targetPower;
            arm.setPower(targetPower);
            return true;
        } else {
            lastArmPower += ARM_POWER_INCREMENT * Math.signum(diff);
            arm.setPower(lastArmPower);
            return false;
        }
    }

    public void armInSmooth(double basePower) {
        armPowerToward(armInPower(basePower));
    }

    public void armOutSmooth(double basePower) {
        armPowerToward(armOutPower(basePower));
    }

    public boolean armStopSmooth() {
        return armPowerToward(0);
    }
}
