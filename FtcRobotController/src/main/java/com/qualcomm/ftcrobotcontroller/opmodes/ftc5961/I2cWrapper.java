package com.qualcomm.ftcrobotcontroller.opmodes.ftc5961;

import com.qualcomm.robotcore.hardware.I2cController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class I2cWrapper implements I2cController.I2cPortReadyCallback {
    private I2cController controller;
    private int port;
    private int addr;
    private byte[] readCache;
    private byte[] writeCache;
    private Lock readCacheLock;
    private Lock writeCacheLock;
    // The first register in the cache as it is currently configured
    private int cacheStartAddress;
    // The last register in the currently configured cache
    private int cacheEndAddress;
    private Queue<I2cAction> actionQueue;

    public I2cWrapper(I2cController controller, int port, int address) {
        this.controller = controller;
        this.port = port;

        // I2C slave devices have seven-bit addresses, which are transmitted in
        // a byte with the "read / write bit" specifying what type of operation
        // is requested. The FTC SDK expects addresses to be bitshifted left by
        // one place to leave space at the end for the read / write bit.
        addr = address << 1;

        readCache = controller.getI2cReadCache(port);
        writeCache = controller.getI2cWriteCache(port);
        readCacheLock = controller.getI2cReadCacheLock(port);
        writeCacheLock = controller.getI2cWriteCacheLock(port);
        writeCacheLock = controller.getI2cWriteCacheLock(port);

        // No read has been configured yet, so make every register invalid.
        cacheStartAddress = 0;
        cacheEndAddress = -1;

        actionQueue = new LinkedList<I2cAction>();

        controller.registerForI2cPortReadyCallback(this, port);
    }

    public int getUnsignedShort(int registerLow, int registerHigh) {
        validateRegister(registerLow);
        validateRegister(registerHigh);
        try {
            int lAddr = registerLow - cacheStartAddress;
            int hAddr = registerHigh - cacheStartAddress;
            readCacheLock.lock();
            int l = readCache[I2cController.I2C_BUFFER_START_ADDRESS + lAddr];
            int h = readCache[I2cController.I2C_BUFFER_START_ADDRESS + hAddr];
            // Mask away the effects of signed conversion
            l &= 0xFF;
            h &= 0xFF;
            return l | (h << 8);
        } finally {
            readCacheLock.unlock();
        }
    }

    public int getSignedShort(int registerLow, int registerHigh) {
        int result = getUnsignedShort(registerLow, registerHigh);
        if ((result & (1 << 15)) != 0) {
            // The sign bit of the short was high.
            // Set the sign bit and the rest of the upper half of the int.
            result |= -1 << 16;
        }
        return result;
    }

    public boolean isAvailable(int register) {
        return cacheStartAddress <= register && register <= cacheEndAddress;
    }

    private void validateRegister(int register) {
        if (!isAvailable(register)) {
            String msg = String.format("Not set up to read 0x%02X", register);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void portIsReady(int eventPort) {
        if (eventPort != port) {
            // We got someone else's event somehow; don't do anything.
            return;
        }

        I2cAction nextAction = actionQueue.poll();
        if (nextAction != null) {
            nextAction.act();
        } else {
            // Assume we're in a repeated read state

            // Update our local copy of the cache
            controller.readI2cCacheFromController(port);

            // Tell the controller there are things it should be doing
            controller.setI2cPortActionFlag(port);
            // In general, we don't want to write the whole cache, because that
            // might overwrite data we'll want to read.
            controller.writeI2cPortFlagOnlyToController(port);
        }
    }

    public interface I2cAction {
        void act();
    }

    public class I2cWrite implements  I2cAction {
        private int target;
        private byte[] data;

        public I2cWrite(int startRegister, byte... data) {
            target = startRegister;
            this.data = Arrays.copyOf(data, data.length);
        }

        @Override
        public void act() {
            controller.enableI2cWriteMode(port, addr, target, data.length);

            try {
                writeCacheLock.lock();
                System.arraycopy(data, 0,
                        writeCache, I2cController.I2C_BUFFER_START_ADDRESS,
                        data.length);
            } finally {
                writeCacheLock.unlock();
            }

            // Indicate that action must be taken by the controller
            controller.setI2cPortActionFlag(port);
            // Send the entire cache, because we must include the data that we
            // want to write and the metadata describing the write.
            controller.writeI2cCacheToController(port);
        }
    }

    public class I2cReadSetup implements I2cAction {
        private int startAddress;
        private int endAddress;

        public I2cReadSetup(int fromRegister, int toRegister) {
            startAddress = fromRegister;
            endAddress = toRegister;
        }

        @Override
        public void act() {
            int length = endAddress - startAddress + 1;

            // Set up the read metadata in the local copy of the cache
            controller.enableI2cReadMode(port, addr, startAddress, length);
            // Send the entire cache so that the controller gets the metadata
            controller.writeI2cCacheToController(port);

            cacheStartAddress = startAddress;
            cacheEndAddress = endAddress;
        }
    }

    public void addAction(I2cAction action) {
        actionQueue.add(action);
    }

    public boolean areActionsFinished() {
        return actionQueue.isEmpty();
    }
}
