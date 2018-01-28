package com.alipchansky.chat.transport;

import com.alipchansky.chat.exceptions.TransportException;

public interface AbstractTransport {
    
    void sendTextMessage (String host, int port, String message) throws TransportException;

}
