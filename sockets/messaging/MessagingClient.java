package messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MessagingClient {

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 5000;

        Socket socket = new Socket(host, port);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Client: ");
            String messageFromClient = scanner.nextLine();
            writer.println(messageFromClient);

            if (messageFromClient.equalsIgnoreCase("sair")) {
                break;
            }

            String messageFromServer = reader.readLine();
            if (messageFromServer == null) {
                break;
            }
            System.out.println("Server: " + messageFromServer);
        }

        scanner.close();
        socket.close();
    }
}
