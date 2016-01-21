package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AutonomousBlue extends OpMode {
    public enum State {
        INIT,
        DRIVE_FROM_WALL,
        TURN_PARALLEL_TO_DIAGONAL,
        TRAVERSE_DIAGONAL,
        TURN_TOWARD_BEACON,
        APPROACH_BEACON,
        DROP_CLIMBERS,
        BACK_UP,
        READ_BEACON,
        PRESS_BUTTON,
        RETREAT,
        GIVE_UP,
        DONE
    }

    protected boolean blue;

    private MainRobot robot;
    private State state;

    private double offset;
    private int pressLeft;

    public AutonomousBlue() {
        blue = true;
    }

    @Override
    public void init() {
        robot = new MainRobot(hardwareMap);
        state = State.INIT;
    }

    @Override
    public void loop() {
        telemetry.addData("state", state);
        double target;
        switch (state) {
            case INIT:
                robot.imu.zeroHeading();
                offset = robot.drive.sumEncoders();
                state = State.DRIVE_FROM_WALL;
                state = State.DRIVE_FROM_WALL;
                break;
            case DRIVE_FROM_WALL:
                robot.sweeper.out();
                if (Math.abs(robot.drive.sumEncoders() - offset) > 8000) {
                    robot.imu.zeroHeading();
                    state = State.TURN_PARALLEL_TO_DIAGONAL;
                    loop();
                } else {
                    robot.driveDirected(0.3, 0, state.ordinal());
                }
                break;
            case TURN_PARALLEL_TO_DIAGONAL:
                if (blue) {
                    target = 45;
                } else {
                    target = 315;
                }
                if (robot.pointTurn(target, state.ordinal())) {
                    offset = robot.drive.sumEncoders();
                    state = State.TRAVERSE_DIAGONAL;
                    loop();
                }
                break;
            case TRAVERSE_DIAGONAL:
                int diagonalDistance;
                if (blue) {
                    target = 45;
                    diagonalDistance = 37000;
                } else {
                    target = 315;
                    diagonalDistance = 25500;
                }
                double distance = Math.abs(robot.drive.sumEncoders() - offset);
                if (distance > diagonalDistance) {
                    robot.imu.zeroHeading();
                    state = State.TURN_TOWARD_BEACON;
                    loop();
                } else {
                    double power;
                    if (distance > (diagonalDistance - 2000) || distance < 2000) {
                        power = 0.3;
                    } else if (distance > (diagonalDistance - 4000) || distance < 4000) {
                        power = 0.6;
                    } else {
                        power = 0.9;
                    }
                    robot.driveDirected(power, target, state.ordinal());
                }
                break;
            case TURN_TOWARD_BEACON:
                if (blue) {
                    target = 45;
                } else {
                    target = 315;
                }
                robot.beaconFeeler.set(true);
                if (robot.pointTurn(target, state.ordinal())) {
                    offset = getRuntime();
                    robot.imu.zeroHeading();
                    state = State.APPROACH_BEACON;
                    loop();
                }
                break;
            case APPROACH_BEACON:
                if (robot.touch.getState()) {
                    robot.drive.setPower(0, 0);
                    robot.sweeper.stop();
                    robot.climberDropper.set(true);
                    offset = getRuntime();
                    state = State.DROP_CLIMBERS;
                } else if (getRuntime() - offset > 10) {
                    robot.drive.setPower(0, 0);
                    robot.sweeper.stop();
                    state = State.GIVE_UP;
                } else {
                    robot.driveDirected(0.2, 0, state.ordinal());
                }
                break;
            case DROP_CLIMBERS:
                if (getRuntime() - offset > 5) {
                    robot.climberDropper.set(false);
                    robot.imu.zeroHeading();
                    offset = robot.drive.sumEncoders();
                    state = State.BACK_UP;
                } else if (getRuntime() - offset > 2) {
                    robot.climberDropper.set(getRuntime() % 0.4 < 0.2);
                }
                break;
            case BACK_UP:
                if (Math.abs(robot.drive.sumEncoders() - offset) > 1000) {
                    robot.drive.setPower(0, 0);
                    robot.beaconFeeler.set(false);
                    // The button pusher doubles as a color sensor arm.
                    //robot.buttonPusherRight.set(true);
                    offset = getRuntime();
                    state = State.DONE;
                } else {
                    robot.driveDirected(-0.3, 0, state.ordinal());
                }
                break;
            case READ_BEACON:
                if (getRuntime() - offset > 1) {
                    robot.buttonPusherRight.set(false);
                    offset = getRuntime();
                    state = State.PRESS_BUTTON;
                } else {
                    int lR = robot.colorLeft.red();
                    int lB = robot.colorLeft.blue();
                    int rR = robot.colorRight.red();
                    int rB = robot.colorRight.blue();
                    boolean redLeft = lR + rB > rR + lB;
                    if (redLeft ^ blue) {
                        pressLeft++;
                    } else {
                        pressLeft--;
                    }
                }
                break;
            case PRESS_BUTTON:
                if (pressLeft > 0) {
                    robot.buttonPusherLeft.set(true);
                } else {
                    robot.buttonPusherRight.set(true);
                }
                robot.driveDirected(0.3, 0, state.ordinal());
                if (getRuntime() - offset > 1) {
                    robot.imu.zeroHeading();
                    offset = robot.drive.sumEncoders();
                    state = State.RETREAT;
                    loop();
                }
                break;
            case RETREAT:
                robot.driveDirected(-0.5, 0, state.ordinal());
                if (robot.drive.sumEncoders() - offset > 2000) {
                    robot.buttonPusherLeft.set(false);
                    robot.buttonPusherRight.set(false);
                    robot.drive.setPower(0, 0);
                    state = State.DONE;
                }
                break;
            case GIVE_UP:
            case DONE:
                break;
            default:
                throw new AssertionError("Unexpected state " + state);
        }
    }
}
