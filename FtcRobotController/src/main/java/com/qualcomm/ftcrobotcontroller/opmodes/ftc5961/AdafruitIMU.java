package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.I2cController;

public class AdafruitIMU {
    public static final int I2C_ADDRESS = 0x28;
    public static final int OPR_MODE = 0x3D;
    public static final byte CONFIGMODE = 0b0000;
    public static final byte IMU = 0b1000;
    public static final byte NDOF = 0b1100;
    public static final int UNIT_SEL = 0x3B;
    public static final byte EULER_DEGREES = 0b000;
    public static final byte OUTPUT_ANDROID = (byte) 0b10000000;
    public static final int EUL_Pitch_MSB = 0x1F;
    public static final int EUL_Pitch_LSB = 0x1E;
    public static final int EUL_Roll_MSB = 0x1D;
    public static final int EUL_Roll_LSB = 0x1C;
    public static final int EUL_Heading_MSB = 0x1B;
    public static final int EUL_Heading_LSB = 0x1A;

    private I2cWrapper i2c;

    public AdafruitIMU(I2cController controller, int port) {
        i2c = new I2cWrapper(controller, port, I2C_ADDRESS);
        byte units = EULER_DEGREES | OUTPUT_ANDROID;
        i2c.addAction(i2c.new I2cWrite(UNIT_SEL, units));
        i2c.addAction(i2c.new I2cWrite(OPR_MODE, IMU));
        i2c.addAction(i2c.new I2cReadSetup(EUL_Heading_LSB, EUL_Pitch_MSB));
    }

    public double pitch() {
        return i2c.getSignedShort(EUL_Pitch_LSB, EUL_Pitch_MSB) / 16.0;
    }

    public double roll() {
        return i2c.getSignedShort(EUL_Roll_LSB, EUL_Roll_MSB) / 16.0;
    }

    public double heading() {
        return i2c.getSignedShort(EUL_Heading_LSB, EUL_Heading_MSB) / 16.0;
    }

    public boolean isReady() {
        return i2c.areActionsFinished();
    }
}
