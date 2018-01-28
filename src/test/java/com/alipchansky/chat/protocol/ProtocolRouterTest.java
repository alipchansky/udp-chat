package com.alipchansky.chat.protocol;

import static org.junit.Assert.*;

import org.junit.Test;

import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.exceptions.UnknownMessageType;
import com.alipchansky.chat.protocol.dummy.DummyTransport;
import com.alipchansky.chat.transport.AbstractTransport;
import com.alipchansky.chat.ui.AbstractChatUI;
import com.alipchansky.chat.ui.DummyUI;

public class ProtocolRouterTest {
    
    String sent = "";
    
    @Test
    public void testRegisterParticipant () throws UnknownMessageType {
        ProtocolRouter router = new ProtocolRouter (new DummyTransport (), new DummyUI ());
        router.translateIncomeMessage("COMMAND:REGISTER:", "192.168.1.1", 9200);
        assertEquals ("192.168.1.1:9200", router.getParticipants());
        router.translateIncomeMessage("COMMAND:REGISTER:", "192.168.1.2", 9201);
        assertEquals ("192.168.1.1:9200,192.168.1.2:9201", router.getParticipants());
    }
    
    
    @Test
    public void testBroadcastParticipants () throws UnknownMessageType {
        ProtocolRouter router = new ProtocolRouter (new DummyTransport (), new DummyUI ());
        router.translateIncomeMessage("COMMAND:PARTICIPANTS:192.168.1.1:9200,192.168.1.2:9201", "-", 9200);
        String participants = router.getParticipants();
        assertEquals ("192.168.1.1:9200,192.168.1.2:9201", participants);
    }
    
    
    @Test
    public void testBroadcastParticipantsOnRegisterParticipant () throws UnknownMessageType {
        
        AbstractTransport transport = new AbstractTransport () {
            @Override
            public void sendTextMessage(String host, int port, String message) throws TransportException {
                sent = message;
            }
        };
        
        ProtocolRouter router = new ProtocolRouter (transport, new DummyUI ());
        router.translateIncomeMessage("COMMAND:REGISTER:", "192.168.1.1", 9200);
        assertEquals ("COMMAND:PARTICIPANTS:192.168.1.1:9200", sent);
        
        router.translateIncomeMessage("COMMAND:REGISTER:", "192.168.1.2", 9201);
        assertEquals ("COMMAND:PARTICIPANTS:192.168.1.1:9200,192.168.1.2:9201", sent);
    }
    
    @Test
    public void testChatMessage () throws UnknownMessageType {
        
        AbstractChatUI chatUi = new AbstractChatUI () {

            @Override
            public void displayMessage(String message, String host, int port) {
                sent = message;
            }

            @Override
            public void setRouter(ProtocolRouter router) {
            }
            
        };
        
        ProtocolRouter router = new ProtocolRouter (new DummyTransport (), chatUi);
        String message = "test message";
        router.translateIncomeMessage("CHAT:"+message, "192.168.1.1", 9200);
        assertEquals (message, sent);
    }
 }
