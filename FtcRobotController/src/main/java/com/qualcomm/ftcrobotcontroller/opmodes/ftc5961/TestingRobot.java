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
    public final Servo climbers;
    public final AdafruitColorSensor leftColor;
    public final AdafruitColorSensor rightColor;
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

        climbers = map.servo.get("climbers");
        climbers.setPosition(0);

        DeviceInterfaceModule dim = map.deviceInterfaceModule.get("CDI");
        leftColor = new AdafruitColorSensor(dim, 0);
        imu = new AdafruitIMU(dim, 1);

        DeviceInterfaceModule dim2 = map.deviceInterfaceModule.get("CDIExtra");
        rightColor = new AdafruitColorSensor(dim2, 0);
    }
}
