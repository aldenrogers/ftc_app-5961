package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

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
    private int armOffset;

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

        bucket = map.servo.get("S1");
        bucket.setPosition(0);

        DeviceInterfaceModule dim = map.deviceInterfaceModule.get("CDI");
        leftColor = new AdafruitColorSensor(dim, 0);
        imu = new AdafruitIMU(dim, 1);

        DeviceInterfaceModule dim2 = map.deviceInterfaceModule.get("CDIExtra");
        rightColor = new AdafruitColorSensor(dim2, 0);
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

    public void armIn() {
        arm.setPower(0.21 + 0.2 * Math.cos(getArmPosition()));
    }

    public void armOut() {
        arm.setPower(-0.21 + 0.2 * Math.cos(getArmPosition()));
    }
}
