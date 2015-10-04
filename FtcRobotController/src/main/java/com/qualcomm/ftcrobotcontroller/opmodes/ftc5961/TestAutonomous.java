package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TestAutonomous extends LinearOpMode {
    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    private TankDrive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("M1");
        leftBack = hardwareMap.dcMotor.get("M3");
        rightFront = hardwareMap.dcMotor.get("M2");
        rightBack = hardwareMap.dcMotor.get("M4");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        drive = new TankDrive();
        drive.addLeftMotor(leftFront);
        drive.addLeftMotor(leftBack);
        drive.addRightMotor(rightFront);
        drive.addRightMotor(rightBack);

        OrientationTracker ot = new OrientationTracker(hardwareMap.appContext);
        ot.start();

        waitForStart();

        int encoderStart = leftFront.getCurrentPosition();
        drive.setPower(0.5, 0.5);
        while (Math.abs(leftFront.getCurrentPosition() - encoderStart) < 5000) {
            waitForNextHardwareCycle();
        }

        float azimuthStart = ot.orientation[0];
        drive.setPower(0.5, 0);
        while (Math.abs(ot.orientation[0] - azimuthStart) < 1) {
            waitForNextHardwareCycle();
        }

        drive.setPower(0, 0);

        ot.stop();
    }
}
