package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.I2cController;

public class AdafruitColorSensor {
    public static final int I2C_ADDRESS = 0x29;
    public static final int ENABLE = 0x00;
    public static final byte ENABLE_PON = 0b1;
    public static final byte ENABLE_AEN = 0b10;
    public static final int CDATAL = 0x14;
    public static final int CDATAH = 0x15;
    public static final int RDATAL = 0x16;
    public static final int RDATAH = 0x17;
    public static final int GDATAL = 0x18;
    public static final int GDATAH = 0x19;
    public static final int BDATAL = 0x1A;
    public static final int BDATAH = 0x1B;

    private I2cWrapper i2c;

    public AdafruitColorSensor(I2cController controller, int port) {
        i2c = new I2cWrapper(controller, port, I2C_ADDRESS);
        byte ponaen = ENABLE_PON | ENABLE_AEN;
        i2c.addAction(i2c.new I2cWrite(ENABLE, ponaen));
        i2c.addAction(i2c.new I2cReadSetup(CDATAL, BDATAH));
    }

    public int clear() {
        return i2c.getShort(CDATAL, CDATAH);
    }

    public int red() {
        return i2c.getShort(RDATAL, RDATAH);
    }

    public int green() {
        return i2c.getShort(GDATAL, GDATAH);
    }

    public int blue() {
        return i2c.getShort(BDATAL, BDATAH);
    }

    public boolean isReady() {
        return i2c.areActionsFinished();
    }
}
