package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class MainRobot {
    public final TankDrive drive;
    public final Lift lift;
    public final Sweeper sweeper;
    public final BucketRotate bucketRotate;
    public final BucketSlide bucketSlide;
    public final Servo churroLock;
    public final WritableBit ziplinerTrigger;
    public final WritableBit climberDropper;
    public final WritableBit climberDropperHigh;
    public final WritableBit buttonPusherLeft;
    public final WritableBit buttonPusherRight;
    public final Servo bp;
    public final WritableBit beaconFeeler;
    public final AdafruitIMU imu;
    public final AdafruitColorSensor colorLeft;
    public final AdafruitColorSensor colorRight;
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

        // TODO: values for zip, churroLock, climbers, button pusher, beacon feeler
        ServoController sc1 = map.servoController.get("SC1");
        ServoController sc2 = map.servoController.get("SC2");
        bucketSlide = new BucketSlide(new Servo(sc1, 1));
        bucketRotate = new BucketRotate(new Servo(sc1, 2));
        bucketRotate.setRotation(0);
        Servo zip = new Servo(sc1, 3);
        zip.setPosition(0);
        ziplinerTrigger = new BinaryServo(zip, 0.3, 0.7);
        ziplinerTrigger.set(false);
        //churroLock = new BinaryServo(new Servo(sc1, 4), 0, 1);
        churroLock = new Servo(sc1, 4);
        churroLock.setPosition(1);
        // Although the button pusher is actually a single mechanism, it can be
        // represented by two writable bits, because at most one will be set at
        // a time.
        Servo buttonPusher = new Servo(sc1, 5);
        bp = buttonPusher;
        buttonPusherLeft = new BinaryServo(buttonPusher, 0, 0.6);
        buttonPusherRight = new BinaryServo(buttonPusher, 0, 0.9);
        Servo cd = new Servo(sc1, 6);
        climberDropper = new BinaryServo(cd, 0, 1);
        climberDropperHigh = new BinaryServo(cd, 0, 0.5);
        beaconFeeler = new BinaryServo(new Servo(sc2, 1), 0, 0.4);

        DeviceInterfaceModule cdi = map.deviceInterfaceModule.get("CDI");
        imu = new AdafruitIMU(cdi, 1);
        colorLeft = new AdafruitColorSensor(cdi, 2);
        I2cController ltc4316 = new I2cAddressTranslator(cdi, 0b1111);
        colorRight = new AdafruitColorSensor(ltc4316, 3);
        touch = new DigitalChannel(cdi, 7);
    }

    public double driveDirected(double power, double direction, int id) {
        return drive.directed(power, imu.relativeHeading(), direction, id);
    }

    public boolean pointTurn(double target, int id) {
        double pidOutput = driveDirected(0, target, id);
        double heading = imu.relativeHeading();
        double error = AdafruitIMU.differenceMod(target, heading, 360);
        return (error < 1 && pidOutput < 0.01);
    }

    public class BucketRotate {
        public final Servo servo;

        private BucketRotate(Servo servo) {
            this.servo = servo;
            setRotation(0);
        }

        public void setRotation(double rotation) {
            servo.setPosition(rotation * 0.4 + 0.4);
        }
    }

    public class BucketSlide {
        private final Servo servo;

        private BucketSlide(Servo servo) {
            this.servo = servo;
            stop();
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
            motor.setPower(1);
        }

        public void out() {
            motor.setPower(-1);
        }

        public void stop() {
            motor.setPower(0);
        }
    }
}
