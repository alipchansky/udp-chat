package com.alipchansky.chat.transport;

import static org.junit.Assert.*;

import java.net.SocketException;

import org.junit.Test;

import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.protocol.ProtocolRouter;
import com.alipchansky.chat.protocol.dummy.DummyTransport;
import com.alipchansky.chat.ui.AbstractChatUI;

public class ChatTransportTest {
    
    String received;
    
    @Test
    public void testSendMessage () throws SocketException, TransportException, InterruptedException {
        AbstractChatUI ui = new AbstractChatUI () {
            @Override
            public void displayMessage(String message, String host, int port) {
                received = message;
            }

            @Override
            public void setRouter(ProtocolRouter router) {
                
            }
        };
        
        ProtocolRouter router = new ProtocolRouter (new DummyTransport(), ui);
        
        ChatTransport transport = new ChatTransport(9090, router);
        transport.start();
        
        transport.sendTextMessage("localhost", 9090, "CHAT:test message");
        
        
        Thread.sleep(1000l);
        
        transport.stop ();
        assertEquals ("test message", received);
        
    }
    

}
