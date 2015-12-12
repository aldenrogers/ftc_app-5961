package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.Servo;

public class BinaryServo implements WritableBit {
    public final Servo servo;
    private final double zero;
    private final double one;

    public BinaryServo(Servo servo, double falsePos, double truePos) {
        this.servo = servo;
        zero = falsePos;
        one = truePos;
    }

    @Override
    public void set(boolean value) {
        servo.setPosition(value ? one : zero);
    }
}
