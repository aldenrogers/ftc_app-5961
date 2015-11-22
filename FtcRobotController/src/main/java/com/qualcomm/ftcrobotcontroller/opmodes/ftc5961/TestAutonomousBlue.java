package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TestAutonomousBlue extends OpMode {
    protected boolean blue;

    private int state;
    private TestingRobot robot;
    private double offset;
    private boolean goLeft;

    @Override
    public void init() {
        state = 0;
        blue = true;
        robot = new TestingRobot(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("state", state);
        double heading;
        switch (state) {
            case 0:
                offset = robot.drive.sumEncoders();
                robot.imu.zeroHeading();
                state++;
                break;
            case 1:
                if (Math.abs(robot.drive.sumEncoders() - offset) > 3000) {
                    state++;
                } else {
                    robot.driveDirected(0.3, 0, state);
                }
                break;
            case 2:
            case 5:
                robot.imu.zeroHeading();
                if (blue) {
                    robot.drive.setPower(0.3, 0);
                } else {
                    robot.drive.setPower(0, 0.3);
                }
                state++;
                break;
            case 3:
            case 6:
                heading = robot.imu.relativeHeading();
                if (40 < heading && heading < 320) {
                    // Robot has turned >40 degrees in whichever direction
                    offset = robot.drive.sumEncoders();
                    robot.drive.setPower(0.3, 0.3);
                    state++;
                }
                break;
            case 4:
                int diagonalDistance;
                if (blue) {
                    diagonalDistance = 28000;
                } else {
                    diagonalDistance = 30000;
                }
                double distance = Math.abs(robot.drive.sumEncoders() - offset);
                if (distance > diagonalDistance) {
                    state++;
                } else {
                    double power;
                    if (distance > (diagonalDistance - 2000) || distance < 2000) {
                        power = 0.3;
                    } else if (distance > (diagonalDistance - 4000) || distance < 4000) {
                        power = 0.6;
                    } else {
                        power = 0.9;
                    }
                    if (blue) {
                        robot.driveDirected(power, 45, state);
                    } else {
                        robot.driveDirected(power, 315, state);
                    }
                }
                break;
            case 7:
                if (robot.touch.isPressed()) {
                    robot.drive.setPower(0, 0);
                    state++;
                } else {
                    if (blue) {
                        robot.driveDirected(0.2, 45, state);
                    } else {
                        robot.driveDirected(0.2, 315, state);
                    }
                }
                break;
            case 8:
                state = 11; // Skip beacon / color stuff
                break;
            case 9:
                int lR = robot.leftColor.red();
                int lB = robot.leftColor.blue();
                int rR = robot.rightColor.red();
                int rB = robot.rightColor.blue();
                boolean redLeft = lR - lB > rR - rB;
                goLeft = redLeft ^ blue;
                offset = getRuntime();
                state++;
                break;
            case 10:
                if (goLeft) {
                    robot.drive.setPower(0.5, 0);
                } else {
                    robot.drive.setPower(0, 0.5);
                }
                if (getRuntime() - offset > 0.5) {
                    robot.drive.setPower(0, 0);
                    offset = getRuntime();
                    state++;
                }
                break;
            case 11:
                if (robot.getArmPosition() < 0.8 || getRuntime() - offset > 5) {
                    offset = getRuntime();
                    robot.bucket.setPosition(0);
                    state++;
                } else {
                    robot.armOutSmooth(0.1);
                    robot.levelBucket();
                    telemetry.addData("Arm position", robot.getArmPosition());
                    telemetry.addData("Net time", getRuntime() - offset);
                }
                break;
            case 12:
                robot.armStopSmooth();
                if (getRuntime() - offset > 5) {
                    state++;
                } else if (getRuntime() - offset > 2.5) {
                    robot.bucket.setPosition(0);
                } else if (getRuntime() - offset > 2) {
                    robot.bucket.setPosition(0.3);
                }
                break;
            case 13:
                if (robot.getArmPosition() > 2) {
                    state++;
                } else {
                    robot.armInSmooth(0.15);
                    robot.levelBucket();
                }
                break;
            case 14:
                if (robot.armStopSmooth()) {
                    state++;
                }
                break;
            case 15:
                break;
            default:
                throw new RuntimeException("Invalid state " + state);
        }
    }
}
