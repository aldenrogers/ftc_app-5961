package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumDrive {
    private DcMotor lf;
    private DcMotor lb;
    private DcMotor rf;
    private DcMotor rb;

    public MecanumDrive(DcMotor leftFront, DcMotor leftBack,
                        DcMotor rightFront, DcMotor rightBack) {
        lf = leftFront;
        lb = leftBack;
        rf = rightFront;
        rb = rightBack;
    }

    public void setComponents(double x, double y, double turn) {
        double magnitude = Math.sqrt(x*x + y*y);
        double direction = Math.atan2(y, x) - Math.PI / 4;
        setVector(magnitude, direction, turn);
    }

    public void setVector(double magnitude, double direction, double turn) {
        if (Math.abs(magnitude) + Math.abs(turn) > 1) {
            magnitude = magnitude / (Math.abs(magnitude) + Math.abs(turn));
            turn = Math.signum(turn) * (1 - Math.abs(magnitude));
        }
        lf.setPower(magnitude * Math.cos(direction) + turn);
        lb.setPower(magnitude * Math.sin(direction) + turn);
        rf.setPower(magnitude * Math.sin(direction) - turn);
        rb.setPower(magnitude * Math.cos(direction) - turn);
    }
}
