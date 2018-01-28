package com.alipchansky.chat.app;

import java.net.SocketException;

import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.protocol.ProtocolRouter;
import com.alipchansky.chat.transport.ChatTransport;
import com.alipchansky.chat.ui.AbstractChatUI;

public class ChatApplication {
    
    private String dispatcherHost;
    private int dispatcherPort;
    ProtocolRouter router;
    ChatTransport transport;
    private int chatPort;

    public ChatApplication (String dispatcherHost, int dispatcherPort, int chatPort, AbstractChatUI ui) {
        this.dispatcherHost = dispatcherHost;
        this.dispatcherPort = dispatcherPort;
        router = new ProtocolRouter(ui);
        transport = new ChatTransport(chatPort, router);
        this.chatPort = chatPort;
        router.setTransport(transport);
        ui.setRouter (router);
    }
    
    public void start () throws SocketException, TransportException {
        transport.start();
        transport.sendTextMessage(dispatcherHost, dispatcherPort, "COMMAND:REGISTER:"+chatPort);
    }
    
    public void stop () {
        transport.stop ();
    }
    
    public String getParticipants () {
        return router.getParticipants();
    }

    public void sendChatMessage(String sentence) {
        router.sendChatMessageToAll(sentence);
        
    }
    
}
