package com.github.tavalin.orvibo.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tavalin.orvibo.OrviboClient;
import com.github.tavalin.orvibo.devices.AllOne;
import com.github.tavalin.orvibo.devices.DeviceType;
import com.github.tavalin.orvibo.devices.OrviboDevice;
import com.github.tavalin.orvibo.devices.Socket;
import com.github.tavalin.orvibo.protocol.Message;

public class PowerHandler extends AbstractCommandHandler {

    private final Logger logger = LoggerFactory.getLogger(PowerHandler.class);
    private int RESPONSE_LENGTH = 23;
    private int DEVICE_START = 0;
    private int DEVICE_END = 6;

    public PowerHandler(OrviboClient client) {
        super(client);
    }

    private void handleAllOne(AllOne allOne, Message message) {
        // nothing to do as far as I can tell
    }

    private void handleSocket(Socket socket, Message message) {
        updatePowerState(socket, message);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    protected int getDeviceStart(){
        return DEVICE_START;
    }
    
    protected int getDeviceEnd() {
        return DEVICE_END;
    }


    @Override
    public boolean isValidResponse(Message message) {
        boolean isValid = false;
        byte[] bytes = message.asBytes();
        isValid = bytes.length == RESPONSE_LENGTH;
        return isValid;
    }


    @Override
    protected void handleInternal(Message message) {
        logger.debug("Handling incoming message");
        byte[] payload = message.getCommandPayload();
        String deviceId = getDeviceId(payload);
        OrviboDevice device = getDevice(deviceId);
        if (device == null) {
            createDevice(message);
        } else if (device.getDeviceType() == DeviceType.SOCKET) {
            handleSocket((Socket)device,message);
        } else if (device.getDeviceType() == DeviceType.ALLONE) {
            handleAllOne((AllOne) device,message);
        } else {
            logger.warn("Unknown device type");
        }
    }


}
