package messagingthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConcurrentMessagingClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "localhost";
        int port = 5001;

        Socket socket = new Socket(host, port);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        Thread receiveThread = new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Server: " + message);
                    if (message.equalsIgnoreCase("sair")) {
                        break;
                    }
                }
            } catch (IOException ignored) {
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        });

        Thread sendThread = new Thread(() -> {
            try {
                while (true) {
                    String message = scanner.nextLine();
                    writer.println(message);
                    if (message.equalsIgnoreCase("sair")) {
                        socket.close();
                        break;
                    }
                }
            } catch (IOException ignored) {
            }
        });

        receiveThread.start();
        sendThread.start();

        receiveThread.join();
        sendThread.join();

        scanner.close();
    }
}
