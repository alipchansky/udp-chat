package com.alipchansky.chat.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;

import com.alipchansky.chat.Constants;
import com.alipchansky.chat.exceptions.TransportException;
import com.alipchansky.chat.exceptions.UnknownMessageType;
import com.alipchansky.chat.protocol.ProtocolRouter;

public class ChatTransport implements AbstractTransport {
    
    private DatagramSocket reciveSocket;
    private ProtocolRouter router;
    private int port;
    private boolean stopped = false;
    
    public ChatTransport (int port, ProtocolRouter router) {
        this.port = port;
        this.router = router;
    }
    
    public void start () throws SocketException {
        reciveSocket = new DatagramSocket(port);
        
        
        
        new Thread () {
            @Override
            public void run() {
                
                while (!stopped) {
                    try {
                        handleIncome ();
                        
                    } catch (IOException e) {
                        System.out.println("Error reading socket at port "+port);
                    } catch (UnknownMessageType e) {
                        System.out.println("Could route income message");
                    }
                }
                
            };
        }.start ();
        
        System.out.println("Transport listenes for inclome messages at port "+port);
        
    }
    
    
    public void stop () {
        stopped = true;
    }

    @Override
    public void sendTextMessage(String host, int port, String message) throws TransportException {
        try {
            DatagramSocket sendSocet = new DatagramSocket();
            InetAddress IPAdress = InetAddress.getByName(host);
            byte[] sendData;
            sendData = message.getBytes("UTF-8");
            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAdress,port);
            sendSocet.send(sendPacket);
            System.out.println("Success sending messgae '"+message+"' to "+host+':'+port);
        } catch (IOException e) {
            System.out.println("Error sending messgae '"+message+"' to "+host+':'+port);
            e.printStackTrace ();
            throw new TransportException ();
        }
    }
    
    
    public void handleIncome () throws IOException, UnknownMessageType {
        byte[] reciveData = new byte[1024];
        DatagramPacket recivePacket= new DatagramPacket(reciveData,reciveData.length);
        reciveSocket.receive(recivePacket);
        
        InetAddress ipAddress = recivePacket.getAddress();
        
        int port = recivePacket.getPort();
        String host = ipAddress.getHostAddress();
        String  sentence = new String(recivePacket.getData());
        System.out.println("Income message '+sentence+' received at port "+port+" from "+host+":"+port);
        
        
        String content = "";
        Matcher m = Constants.regex.matcher(sentence);
        
        while (m.find()) {
            content = content + (sentence.substring(m.start(),m.end()));
        }
        
        router.translateIncomeMessage(content, host, port);
    }

}
