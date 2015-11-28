package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TestTeleOp extends OpMode {
    private TestingRobot robot;

    @Override
    public void init() {
        robot = new TestingRobot(hardwareMap);
    }

    @Override
    public void loop() {
        robot.drive.setPower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }
}
