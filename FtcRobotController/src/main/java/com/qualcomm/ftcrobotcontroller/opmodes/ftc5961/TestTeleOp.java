package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.hardware.AdafruitColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

public class TestTeleOp extends OpMode {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    private DcMotor slide;
    private TankDrive drive;
    private OrientationTracker ot;
    private DeviceInterfaceModule sensors;
    private AdafruitColorSensor color;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("M1");
        leftBack = hardwareMap.dcMotor.get("M3");
        rightFront = hardwareMap.dcMotor.get("M2");
        rightBack = hardwareMap.dcMotor.get("M4");
        slide = hardwareMap.dcMotor.get("M5");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        drive = new TankDrive();
        drive.addLeftMotor(leftFront);
        drive.addLeftMotor(leftBack);
        drive.addRightMotor(rightFront);
        drive.addRightMotor(rightBack);

        ot = new OrientationTracker(hardwareMap.appContext);
        ot.start();

        sensors = hardwareMap.deviceInterfaceModule.get("CDI");
        color = new AdafruitColorSensor(sensors, 0);
    }

    @Override
    public void loop() {
        drive.setPower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        if (gamepad1.dpad_up) {
            slide.setPower(0.3);
        } else if (gamepad1.dpad_down) {
            slide.setPower(-0.3);
        } else {
            slide.setPower(0);
        }

        telemetry.addData("alpha", color.alpha());
        telemetry.addData("red", color.red());
        telemetry.addData("green", color.green());
        telemetry.addData("blue", color.blue());
    }

    @Override
    public void stop() {
        ot.stop();
    }
}
