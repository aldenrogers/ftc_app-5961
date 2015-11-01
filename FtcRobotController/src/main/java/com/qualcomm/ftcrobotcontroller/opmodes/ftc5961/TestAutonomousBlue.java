package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TestAutonomousBlue extends LinearOpMode {
    protected boolean isBlue() {
        return true;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        TestingRobot robot = new TestingRobot(hardwareMap);

        waitForStart();

        int mult = isBlue() ? -1 : 1;

        int startPos = robot.leftBack.getCurrentPosition();
        robot.drive.setPower(mult * 1, mult * 0.01);
        // TODO: change to BNO055 control
        while (Math.abs(robot.leftBack.getCurrentPosition() - startPos) < 2000) {
            waitForNextHardwareCycle();
        }
        robot.drive.setPower(0, 0);

        startPos = robot.leftBack.getCurrentPosition();
        robot.drive.setPower(mult * 0.3, mult * 0.3);
        // TODO: change to BNO055 control
        while (Math.abs(robot.leftBack.getCurrentPosition() - startPos) < 300) {
            waitForNextHardwareCycle();
        }
        robot.drive.setPower(0, 0);

        robot.climbers.setPosition(1);
        sleep(1000);
        robot.climbers.setPosition(0);

        robot.drive.setPower(0, 0);
        waitOneFullHardwareCycle();
    }
}
