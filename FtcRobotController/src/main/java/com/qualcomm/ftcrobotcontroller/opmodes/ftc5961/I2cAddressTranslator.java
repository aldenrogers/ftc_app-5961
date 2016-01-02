package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.concurrent.locks.Lock;

public class I2cAddressTranslator implements I2cController {
    private final I2cController parent;
    private final int xor;

    public I2cAddressTranslator(I2cController controller, int xorByte) {
        parent = controller;
        xor = xorByte;
    }

    @Override
    public void copyBufferIntoWriteBuffer(int port, byte[] buffer) {
        parent.copyBufferIntoWriteBuffer(port, buffer);
    }

    @Override
    public void deregisterForPortReadyCallback(int port) {
        parent.deregisterForPortReadyCallback(port);
    }

    @Override
    public void enableI2cReadMode(int port, int i2cAddress, int memAddress,
                                  int length) {
        parent.enableI2cReadMode(port, i2cAddress ^ xor, memAddress, length);
    }

    @Override
    public void enableI2cWriteMode(int port, int i2cAddress, int memAddress,
                                   int length) {
        parent.enableI2cWriteMode(port, i2cAddress ^ xor, memAddress, length);
    }
    
    @Override
    public byte[] getCopyOfReadBuffer(int port) {
        return parent.getCopyOfReadBuffer(port);
    }

    @Override
    public byte[] getCopyOfWriteBuffer(int port) {
        return parent.getCopyOfWriteBuffer(port);
    }

    @Override
    public byte[] getI2cReadCache(int port) {
        return parent.getI2cReadCache(port);
    }

    @Override
    public Lock getI2cReadCacheLock(int port) {
        return parent.getI2cReadCacheLock(port);
    }

    @Override
    public byte[] getI2cWriteCache(int port) {
        return parent.getI2cWriteCache(port);
    }

    @Override
    public Lock getI2cWriteCacheLock(int port) {
        return parent.getI2cWriteCacheLock(port);
    }

    @Override
    public SerialNumber getSerialNumber() {
        return parent.getSerialNumber();
    }

    @Override
    public boolean isI2cPortActionFlagSet(int port) {
        return parent.isI2cPortActionFlagSet(port);
    }

    @Override
    public boolean isI2cPortInReadMode(int port) {
        return parent.isI2cPortInReadMode(port);
    }

    @Override
    public boolean isI2cPortInWriteMode(int port) {
        return parent.isI2cPortInWriteMode(port);
    }

    @Override
    public boolean isI2cPortReady(int port) {
        return parent.isI2cPortReady(port);
    }

    @Override
    public void readI2cCacheFromController(int port) {
        parent.readI2cCacheFromController(port);
    }

    @Deprecated
    @Override
    public void readI2cCacheFromModule(int port) {
        parent.readI2cCacheFromModule(port);
    }

    @Override
    public void registerForI2cPortReadyCallback(
            I2cController.I2cPortReadyCallback callback, int port) {
        parent.registerForI2cPortReadyCallback(callback, port);
    }

    @Override
    public void setI2cPortActionFlag(int port) {
        parent.setI2cPortActionFlag(port);
    }

    @Override
    public void writeI2cCacheToController(int port) {
        parent.writeI2cCacheToController(port);
    }

    @Deprecated
    @Override
    public void writeI2cCacheToModule(int port) {
        parent.writeI2cCacheToModule(port);
    }

    @Override
    public void writeI2cPortFlagOnlyToController(int port) {
        parent.writeI2cPortFlagOnlyToController(port);
    }

    @Deprecated
    @Override
    public void writeI2cPortFlagOnlyToModule(int port) {
        parent.writeI2cPortFlagOnlyToModule(port);
    }

    @Override
    public void close() {
        parent.close();
    }

    @Override
    public String getConnectionInfo() {
        return parent.getConnectionInfo();
    }

    @Override
    public String getDeviceName() {
        return parent.getDeviceName();
    }

    @Override
    public int getVersion() {
        return parent.getVersion();
    }
}
