package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TeleOp extends OpMode {
    private MainRobot robot;

    @Override
    public void init() {
        robot = new MainRobot(hardwareMap);
    }

    @Override
    public void loop() {
        robot.drive.setPower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        if (gamepad1.right_bumper) {
            robot.sweeper.in();
        } else if (gamepad1.left_bumper) {
            robot.sweeper.out();
        } else {
            robot.sweeper.stop();
        }

        float r = gamepad2.right_trigger - gamepad2.left_trigger;
        robot.bucketRotate.setRotation(r / 4);

        if (gamepad2.dpad_up) {
            robot.lift.extend();
        } else if (gamepad2.dpad_down) {
            robot.lift.retract();
        } else {
            robot.lift.stopExtension();
        }
        if (gamepad2.y) {
            robot.lift.raise();
        } else if (gamepad2.x) {
            robot.lift.lower();
        } else {
            robot.lift.stopElevation();
        }
        if (gamepad2.dpad_left) {
            robot.bucketSlide.left();
        } else if (gamepad2.dpad_right) {
            robot.bucketSlide.right();
        } else {
            robot.bucketSlide.stop();
        }
    }
}
