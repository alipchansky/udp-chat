package com.alipchansky.chat.dispatcher;

import java.net.SocketException;

import com.alipchansky.chat.protocol.ProtocolRouter;
import com.alipchansky.chat.transport.ChatTransport;

public class ChatDispatcher {
    
    ChatTransport transport;
    ProtocolRouter router;

    public ChatDispatcher (int port) {
        router = new ProtocolRouter ();
        transport = new ChatTransport (port, router);
        router.setTransport(transport);
    }
    
    public void start () throws SocketException {
        transport.start ();
    }
    
    public void stop () {
        transport.stop();
    }
    
    public String getParticipants () {
        return router.getParticipants ();
    }
    

}
