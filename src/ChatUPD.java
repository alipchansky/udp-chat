import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;


import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by PodVALCompany on 20.01.2018.
 */
 class ChatUDP extends JFrame {
     private  JTextArea taMain;
     private  JTextField tfMSG;


      private  final String FRM_TITLE  = "PodVALchat_1.0";
      private final  int FRM_LOC_x = 100;
      private final int FRM_LOC_y = 100;
      private final int FRM_WIDTH=400;
      private final int FRM_HEIGHT=400;

    private  final int PORT = 9786;
    private final String IP_BROADCAST1 = "192.168.0.110";


    private class thdRecriver extends Thread {
        @Override
        public void start() {
            super.start();
            try {
                customize();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        private  void customize () throws Exception {
            DatagramSocket reciveSocket = new DatagramSocket(PORT);
            Pattern regex = Pattern.compile("[\u0020-\uFFFF]");



            while (true){
                byte[] reciveData = new byte[1024];
                DatagramPacket recivePacket= new DatagramPacket(reciveData,reciveData.length);
                reciveSocket.receive(recivePacket);
                InetAddress IPAddress = recivePacket.getAddress();
                int port = recivePacket.getPort();
                String  sentence = new String(recivePacket.getData());
                Matcher m = regex.matcher(sentence);

                taMain.append(IPAddress.toString()  + ":" + port + ": ");

                while (m.find())
                    taMain.append(sentence.substring(m.start(),m.end()));
                taMain.append("\r\n");
                taMain.setCaretPosition(taMain.getText().length());




            }

        };
    }
    private  void btnSend_Handler ()throws Exception {
        DatagramSocket sendSocet =  new DatagramSocket();
        InetAddress IPAdress = InetAddress.getByName(IP_BROADCAST1);
        byte[] sendData;
        String sentence = tfMSG.getText();
        tfMSG.setText("");
        sendData = sentence.getBytes("UTF-8");
        DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAdress,PORT);
        sendSocet.send(sendPacket);
    }

    private void framDraw(JFrame frame ){
        tfMSG=new JTextField();
        taMain=new JTextArea(FRM_HEIGHT/19,50);
        JScrollPane spMain = new JScrollPane(taMain);
        spMain.setLocation(0,0);
        taMain.setLineWrap(true);
        taMain.setEnabled(false);


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
    private void antistatic() {
        framDraw (new ChatUDP());

        new thdRecriver().start();

    }


    public static void main(String[] args) {
        new ChatUDP().antistatic();

    }

}
