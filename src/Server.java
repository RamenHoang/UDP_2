import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.io.IOException;
import java.net.*;

public class Server {
    private DatagramSocket serverSocket;

    public Server() {
        try {
            this.serverSocket = new DatagramSocket(8081);
            System.out.println("Server is running ...");
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                new MyTask(receivePacket);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public class MyTask extends Thread {
        private DatagramPacket receivePacket;
        
        public MyTask(DatagramPacket receivePacket) {
            this.receivePacket = receivePacket;
            this.start();
        }

        @Override
        public void run() {
            this.doMission();
        }

        private void doMission() {
            try {
                System.out.println("Connected");
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String instr = new String(receivePacket.getData(), "UTF-8");
                String res = "";
                	try {
                		res = Evaluation.evaluate(instr) + "";
                		res = res.trim();
                	} catch(Exception e) {
                		System.out.println(e.getMessage());
                		System.out.println(e.getStackTrace());
                	}
                byte[] _sendData = res.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(_sendData, _sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }
}
