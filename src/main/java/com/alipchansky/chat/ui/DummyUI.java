package com.alipchansky.chat.ui;

import com.alipchansky.chat.protocol.ProtocolRouter;

public class DummyUI implements AbstractChatUI {

    @Override
    public void displayMessage(String message, String host, int port) {
    }

    @Override
    public void setRouter(ProtocolRouter router) {
    }

}
