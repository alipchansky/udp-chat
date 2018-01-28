package com.alipchansky.chat;

import java.net.SocketException;

import com.alipchansky.chat.dispatcher.ChatDispatcher;

public class DiispatcherApp {
    
    
    public static void main(String[] args) throws SocketException {
        ChatDispatcher dispatcher = new ChatDispatcher(Constants.DISPATCHER_PORT);
        dispatcher.start();
    }

}
