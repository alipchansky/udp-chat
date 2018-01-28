package com.alipchansky.chat;


import java.awt.BorderLayout;
import java.awt.Color;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.alipchansky.chat.app.ChatApplication;
import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.protocol.ProtocolRouter;
import com.alipchansky.chat.ui.AbstractChatUI;


/**
 * Created by PodVALCompany on 20.01.2018.
 */
 class ChatUDP extends JFrame implements AbstractChatUI {
     
     
     private  JTextArea taMain;
     private  JTextField tfMSG;
     private  final String FRM_TITLE  = "PodVALchat_1.0";
     private final  int FRM_LOC_x = 100;
     private final int FRM_LOC_y = 100;
     private final int FRM_WIDTH=400;
     private final int FRM_HEIGHT=400;
     
     
     ChatApplication application;
    private ProtocolRouter router;
      

    
    private  void btnSend_Handler ()throws Exception {
        String sentence = tfMSG.getText();
        sendChatMessage (sentence);
        tfMSG.setText("");
    }
    


    private void framDraw(JFrame frame ){
        tfMSG=new JTextField();
        taMain=new JTextArea(FRM_HEIGHT/19,50);
        
        JScrollPane spMain = new JScrollPane(taMain);
        spMain.setLocation(0,0);
        taMain.setLineWrap(true);
        taMain.setEnabled(false);
        taMain.setDisabledTextColor(new Color(0));
        


        JButton btnSend = new JButton();
        btnSend.setText("Send");
        btnSend.setToolTipText("Broadcast a message");
        btnSend.addActionListener(e-> {
            try{
                btnSend_Handler();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(FRM_TITLE);
        frame.setLocation(FRM_LOC_x,FRM_LOC_y);
        frame.setSize(FRM_WIDTH,FRM_HEIGHT);
        frame.setResizable(false);
        frame.getContentPane().add(BorderLayout.NORTH,spMain);
        frame.getContentPane().add(BorderLayout.CENTER,tfMSG);
        frame.getContentPane().add(BorderLayout.EAST,btnSend);
        frame.setVisible(true);

    }
    
    private void startChatWindow (int port) throws SocketException, TransportException {
        framDraw (new ChatUDP());
        application = new ChatApplication(Constants.DISPATCHER_HOST, Constants.DISPATCHER_PORT, port, this);
        application.start();
    }

    public static void main(String[] args) throws SocketException, TransportException {
        new ChatUDP().startChatWindow(9093);
    }
        
    
    

    private void drawMessage (String content) {
        taMain.append (content);
        taMain.append("\r\n");
        taMain.setCaretPosition(taMain.getText().length());
    }
    
    
    private void sendChatMessage(String sentence) {
        application.sendChatMessage (sentence);
        
    }

    


    @Override
    public void displayMessage(String message, String host, int port) {
        drawMessage (message);
    }


    @Override
    public void setRouter(ProtocolRouter router) {
        this.router = router;
    }

}
