package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

class JohnsRobot extends OpMode {


    {
        DcMotor leftDrive;
        DcMotor rightDrive;
    }

    @Override
    void init(){
        leftDrive = hardwareMap.dcMotor.get("motor1");
        rightDrive = hardwareMap.dcMotor.get("motor2");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    void loop() {
        if (gamepad1.dpad_up){
            leftDrive.setPower(100);
            rightDrive.setPower(100);
        }
        else if (gamepad1.dpad_right){
            leftDrive.setPower(50);
            rightDrive.setPower(-50);
        }
        else if (gamepad1.dpad_left){
            leftDrive.setPower(-50);
            rightDrive.setPower(50);
        }
        else if (gamepad1.dpad_down){
            leftDrive.setPower(-100);
            rightDrive.setPower(-100);
        }
    }

    @Override
    void stop() {

    }
}