

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public static void main(String[] args) {
        String serverHostname = "phy";
        int serverPort = 4321;

        try {
            System.out.println("Connecting to " + serverHostname + ":" + serverPort);
            Socket socket = new Socket(serverHostname, serverPort);
            System.out.println("Connected to server: " + socket.getRemoteSocketAddress());
            System.out.println("Connected to server: " + socket.getLocalPort());

            //RST tcp connection
            socket.setSoLinger(true, 0);
            // You can now use the socket to communicate with the server
            // For example, you can get the input and output streams and send/receive data
            Thread.sleep(10000);
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverHostname);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not connect to " + serverHostname + ":" + serverPort);
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
