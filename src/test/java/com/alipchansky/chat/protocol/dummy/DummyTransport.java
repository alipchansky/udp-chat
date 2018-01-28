package com.alipchansky.chat.protocol.dummy;

import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.transport.AbstractTransport;

public class DummyTransport implements AbstractTransport {

    @Override
    public void sendTextMessage(String host, int port, String message) throws TransportException {
    }

}
