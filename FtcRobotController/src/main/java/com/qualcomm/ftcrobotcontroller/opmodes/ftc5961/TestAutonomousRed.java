package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

public class TestAutonomousRed extends TestAutonomousBlue {
    @Override
    public void init() {
        super.init();
        blue = false;
    }
}
