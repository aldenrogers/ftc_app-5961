package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class MainRobot {
    public final TankDrive drive;
    public final Lift lift;
    public final Sweeper sweeper;
    public final BucketRotate bucketRotate;
    public final BucketSlide bucketSlide;
    public final AdafruitIMU imu;
    public final DigitalChannel touch;

    public MainRobot(HardwareMap map) {
        drive = new TankDrive();
        DcMotorController mc1 = map.dcMotorController.get("MC1");
        DcMotorController mc2 = map.dcMotorController.get("MC2");
        drive.addRightMotor(new DcMotor(mc1, 1, DcMotor.Direction.REVERSE));
        drive.addRightMotor(new DcMotor(mc2, 1, DcMotor.Direction.REVERSE));
        drive.addLeftMotor(new DcMotor(mc1, 2));
        drive.addLeftMotor(new DcMotor(mc2, 2));

        DcMotorController mc3 = map.dcMotorController.get("MC3");
        DcMotorController mc4 = map.dcMotorController.get("MC4");
        sweeper = new Sweeper(new DcMotor(mc3, 1));
        lift = new Lift(new DcMotor(mc3, 2), new DcMotor(mc4, 1));

        ServoController sc1 = map.servoController.get("SC1");
        bucketRotate = new BucketRotate(new Servo(sc1, 2));
        bucketSlide = new BucketSlide(new Servo(sc1, 3));

        DeviceInterfaceModule cdi = map.deviceInterfaceModule.get("CDI");
        imu = new AdafruitIMU(cdi, 1);
        touch = new DigitalChannel(cdi, 0);
    }

    public double driveDirected(double power, double direction, int id) {
        return drive.directed(power, imu.relativeHeading(), direction, id);
    }

    public class BucketRotate {
        public final Servo servo;

        private BucketRotate(Servo servo) {
            this.servo = servo;
        }

        public void setRotation(double rotation) {
            servo.setPosition(rotation + 0.5);
        }
    }

    public class BucketSlide {
        private final Servo servo;

        private BucketSlide(Servo servo) {
            this.servo = servo;
        }

        public void left() {
            servo.setPosition(0);
        }

        public void stop() {
            servo.setPosition(0.55);
        }

        public void right() {
            servo.setPosition(1);
        }
    }

    public class Lift {
        private final DcMotor extension;
        private final DcMotor elevation;

        private Lift(DcMotor extension, DcMotor elevation) {
            this.extension = extension;
            this.elevation = elevation;
        }

        public void extend() {
            extension.setPower(-1);
        }

        public void retract() {
            extension.setPower(1);
        }

        public void stopExtension() {
            extension.setPower(0);
        }

        public void raise() {
            elevation.setPower(-1);
        }

        public void lower() {
            elevation.setPower(1);
        }

        public void stopElevation() {
            elevation.setPower(0);
        }
    }

    public class Sweeper {
        private final DcMotor motor;

        private Sweeper(DcMotor motor) {
            this.motor = motor;
        }

        public void in() {
            motor.setPower(-1);
        }

        public void out() {
            motor.setPower(1);
        }

        public void stop() {
            motor.setPower(0);
        }
    }
}
