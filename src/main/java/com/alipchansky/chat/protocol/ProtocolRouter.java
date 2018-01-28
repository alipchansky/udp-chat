package com.alipchansky.chat.protocol;

import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.exceptions.UnknownMessageType;
import com.alipchansky.chat.transport.AbstractTransport;
import com.alipchansky.chat.ui.AbstractChatUI;
import com.alipchansky.chat.ui.DummyUI;

public class ProtocolRouter {
    private final static String COMMAND_PREFIX = "COMMAND:";
    private final static String REGISTER_PREFIX = "REGISTER:";
    private final static String PARTICIPANTS_PREFIX = "PARTICIPANTS:";
    private final static String CHAT_PREFIX = "CHAT:";
    
    private String participants = "";
    
    private AbstractTransport transport;
    private AbstractChatUI chatUI;
    
    public String getParticipants () {
        return participants;
    }
    
    public ProtocolRouter (AbstractTransport transport, AbstractChatUI ui) {
        this.transport = transport;
        this.chatUI = ui;
    }
    
    public ProtocolRouter (AbstractChatUI ui) {
        this.chatUI = ui;
        this.transport = null;
    }
    
    public ProtocolRouter() {
        this (new DummyUI ());
    }
    
    public void setTransport (AbstractTransport transport) {
        this.transport = transport;
    }

    public void translateIncomeMessage ( String message, String host, int port ) throws UnknownMessageType {
        
        if (message.startsWith(COMMAND_PREFIX)) processCommand (message.substring(COMMAND_PREFIX.length()), host, port);
        else if (message.startsWith(CHAT_PREFIX)) processChat (message.substring(CHAT_PREFIX.length()), host, port);
        else throw new UnknownMessageType ();
    }

    private void processCommand(String message, String host, int port ) throws UnknownMessageType {
        if (host.startsWith("/")) host = host.substring(1);
        if (message.startsWith(REGISTER_PREFIX)) {
            processRegisterParticipant (host, Integer.valueOf(message.substring(REGISTER_PREFIX.length())));
            processBroadcastParticipants ();
        }
        else if (message.startsWith(PARTICIPANTS_PREFIX)) {
            processImcoemParticipants (message.substring(PARTICIPANTS_PREFIX.length()));
        }else    throw new UnknownMessageType ();
        
    }
    
    private void processImcoemParticipants(String participants) {
        this.participants = participants;
    }


    private void processBroadcastParticipants() {
        sendMessageToAllParticipants(COMMAND_PREFIX+PARTICIPANTS_PREFIX+participants);
    }

    private void sendMessageToAllParticipants(String message) {
        String aParticipants[] = participants.split(",");
        for (String participant : aParticipants) {
            try {
                sendMessageToParticipant(participant, message);
            } catch (TransportException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToParticipant(String participant, String message) throws TransportException {
        String  a[] = participant.split(":");
        String host = a[0];
        String sPort = a[1];
        int port = Integer.valueOf(sPort);
        sendTextMessage (host, port, message);
    }


    protected void sendTextMessage(String host, int port, String message) throws TransportException {
        transport.sendTextMessage(host, port, message);
        
    }


    private String processRegisterParticipant(String host, int port) {
        String participant = host+":"+port;
        if (participants.length()==0)
            participants+=participant;
        else
            participants+=','+participant;
        return participants;
    }

    private void processChat(String message, String host, int port ) {
        chatUI.displayMessage(message, host, port);
    }
    
    public void sendChatMessageToAll (String chatMessage) {
        sendMessageToAllParticipants(CHAT_PREFIX+chatMessage);
    }
    
    
    

    

}
