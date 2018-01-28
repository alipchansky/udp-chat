package com.alipchansky.chat.ui;

import com.alipchansky.chat.protocol.ProtocolRouter;

public interface AbstractChatUI {
    
    void displayMessage (String message, String host, int port);

    void setRouter(ProtocolRouter router);

}
