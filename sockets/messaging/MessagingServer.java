package messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MessagingServer {

    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String messageFromClient = reader.readLine();
            if (messageFromClient == null || messageFromClient.equalsIgnoreCase("sair")) {
                break;
            }
            System.out.println("Client: " + messageFromClient);

            System.out.print("Server: ");
            String messageFromServer = scanner.nextLine();
            writer.println(messageFromServer);
        }

        scanner.close();
        clientSocket.close();
        serverSocket.close();
    }
}
