package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestingRobot {
    public final DcMotor leftFront;
    public final DcMotor leftBack;
    public final DcMotor rightFront;
    public final DcMotor rightBack;
    public final TankDrive drive;
    public final AdafruitIMU imu;

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

        DeviceInterfaceModule dim2 = map.deviceInterfaceModule.get("CDIExtra");
        imu = new AdafruitIMU(dim2, 1);
    }

    /**
     * Convenience method to call
     * drive.{@link TankDrive#directed(double, double, double, int)}
     * using imu.{@link AdafruitIMU#relativeHeading()} as the current heading
     * @param power the maximum drive power
     * @param targetHeading the desired heading, in degrees
     */
    public void driveDirected(double power, double targetHeading, int id) {
        drive.directed(power, imu.relativeHeading(), targetHeading, id);
    }
}
