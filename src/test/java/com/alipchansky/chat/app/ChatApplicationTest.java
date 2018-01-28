package com.alipchansky.chat.app;

import static org.junit.Assert.*;

import java.net.SocketException;

import org.junit.Test;

import com.alipchansky.chat.dispatcher.ChatDispatcher;
import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.ui.DummyUI;

public class ChatApplicationTest {
    private final static int DISPATCHER_PORT = 9090;
    private static final int APP1_PORT = 9091;
    private static final int APP2_PORT = 9092;
    
    
    @Test
    public void test () throws SocketException, TransportException, InterruptedException {
        ChatDispatcher dispatcher = new ChatDispatcher (DISPATCHER_PORT);
        dispatcher.start();
        Thread.sleep(1000l);
        
        ChatApplication app1 = new ChatApplication  ("localhost", DISPATCHER_PORT, APP1_PORT, new DummyUI());
        app1.start();

        ChatApplication app2 = new ChatApplication  ("localhost", DISPATCHER_PORT, APP2_PORT, new DummyUI());
        app2.start();
        
        Thread.sleep(1000l);
        
        assertEquals ("127.0.0.1:9091,127.0.0.1:9092", app1.getParticipants());
        
    }

}
