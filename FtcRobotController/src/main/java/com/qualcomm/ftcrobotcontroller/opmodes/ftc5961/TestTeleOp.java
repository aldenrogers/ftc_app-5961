package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;

public class TestTeleOp extends OpMode {

    DeviceInterfaceModule sensors;
    DcMotor leftFront;
    DcMotor leftBack;
    DcMotor rightFront;
    DcMotor rightBack;
    Servo continuousRotation;
    Servo halfCircle;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("M1");
        leftBack = hardwareMap.dcMotor.get("M3");
        rightFront = hardwareMap.dcMotor.get("M2");
        rightBack = hardwareMap.dcMotor.get("M4");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        halfCircle = hardwareMap.servo.get("S1");
        continuousRotation = hardwareMap.servo.get("S2");
        halfCircle.setPosition(0);

        sensors = hardwareMap.deviceInterfaceModule.get("CDI");
    }

    @Override
    public void loop() {
        // Tank drive
        float leftPower = -gamepad1.left_stick_y;
        float rightPower = -gamepad1.right_stick_y;
        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);
        rightFront.setPower(rightPower);
        rightBack.setPower(rightPower);

        // Set 180 degree servo position in response to button presses
        if (gamepad2.a) {
            halfCircle.setPosition(0);
        } else if (gamepad2.b) {
            halfCircle.setPosition(0.33);
        } else if (gamepad2.y) {
            halfCircle.setPosition(0.67);
        } else if (gamepad2.x) {
            halfCircle.setPosition(1);
        }

        // Move continuous rotation servo depending on button state
        if (gamepad2.left_bumper) {
            continuousRotation.setPosition(0);
        } else if (gamepad2.right_bumper) {
            continuousRotation.setPosition(1);
        } else {
            continuousRotation.setPosition(0.55);
        }

        // Send sensor values to driver station
        telemetry.addData("Touch", sensors.getAnalogInputValue(0) > 512 ? "Pressed" : "Released");
        telemetry.addData("Distance", sensors.getAnalogInputValue(1));
    }

    @Override
    public void stop() {
    }
}
