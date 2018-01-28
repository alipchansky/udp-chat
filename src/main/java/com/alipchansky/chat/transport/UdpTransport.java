package com.alipchansky.chat.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;

import com.alipchansky.chat.Constants;

public class UdpTransport {
    DatagramSocket reciveSocket;
    
    
    public void init () throws SocketException {
        reciveSocket = new DatagramSocket(Constants.PORT);
    } 

    public String transportReceiveContent() throws IOException {
        byte[] reciveData = new byte[1024];
        DatagramPacket recivePacket= new DatagramPacket(reciveData,reciveData.length);
        reciveSocket.receive(recivePacket);
        InetAddress ipAddress = recivePacket.getAddress();
        int port = recivePacket.getPort();
        String  sentence = new String(recivePacket.getData());
        
        
        String content = ipAddress.toString()  + ":" + port + ": ";
        Matcher m = Constants.regex.matcher(sentence);
        while (m.find())
            content = content + (sentence.substring(m.start(),m.end()));
        return content;
    };
    
    public void transportSendSentence (String sentence) throws Exception {
        DatagramSocket sendSocet =  new DatagramSocket();
        InetAddress IPAdress = InetAddress.getByName(Constants.IP_BROADCAST1);
        byte[] sendData;
        sendData = sentence.getBytes("UTF-8");
        DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAdress,Constants.PORT);
        sendSocet.send(sendPacket);
    }
}
