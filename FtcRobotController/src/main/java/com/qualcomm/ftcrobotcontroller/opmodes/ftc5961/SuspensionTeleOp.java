package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class SuspensionTeleOp extends OpMode {
    private SuspensionRobot robot;

    @Override
    public void init() {
        robot = new SuspensionRobot(hardwareMap);
    }

    @Override
    public void loop() {
        robot.drive.setPower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }
}
