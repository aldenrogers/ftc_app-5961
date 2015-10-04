package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TestTeleOp extends OpMode {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    private TankDrive drive;
    private OrientationTracker ot;

    @Override
    public void init() {
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

        ot = new OrientationTracker(hardwareMap.appContext);
        ot.start();
    }

    @Override
    public void loop() {
        drive.setPower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        // Send orientation data to driver station
        telemetry.addData("azimuth", ot.orientation[0]);
        telemetry.addData("pitch", ot.orientation[1]);
        telemetry.addData("roll", ot.orientation[2]);
    }

    @Override
    public void stop() {
        ot.stop();
    }
}
