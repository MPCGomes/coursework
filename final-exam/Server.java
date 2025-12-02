import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started!");
            System.out.println("Port " + PORT + " opened! Waiting for connection...\n");

            while (true) {
                Socket client = server.accept();
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                ReadData readData = new ReadData(client);
                readData.start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    System.err.println("Error closing server: " + e.getMessage());
                }
            }
        }
    }
}