package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SuspensionRobot {
    public final TankDrive drive;

    public SuspensionRobot(HardwareMap map) {
        DcMotor left1 = map.dcMotor.get("L1");
        DcMotor left2 = map.dcMotor.get("L2");
        DcMotor right1 = map.dcMotor.get("R1");
        DcMotor right2 = map.dcMotor.get("R2");
        right1.setDirection(DcMotor.Direction.REVERSE);
        right2.setDirection(DcMotor.Direction.REVERSE);
        drive = new TankDrive();
        drive.addLeftMotor(left1);
        drive.addLeftMotor(left2);
        drive.addRightMotor(right1);
        drive.addRightMotor(right2);
    }
}
