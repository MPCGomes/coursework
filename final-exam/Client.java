import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Socket client = null;
        Scanner input = null;
        PrintStream output = null;
        Scanner keyboard = null;

        try {
            client = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connected to server!\n");

            input = new Scanner(client.getInputStream());
            output = new PrintStream(client.getOutputStream());
            keyboard = new Scanner(System.in);

            while (input.hasNextLine()) {
                String serverMessage = input.nextLine();
                System.out.print(serverMessage);

                if (serverMessage.contains("Data saved successfully!")) {
                    System.out.println();
                    break;
                }

                if (serverMessage.endsWith(": ")) {
                    String userInput = keyboard.nextLine();
                    output.println(userInput);
                }
            }

            System.out.println("Connection closed.");

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        } finally {
            try {
                if (keyboard != null) keyboard.close();
                if (output != null) output.close();
                if (input != null) input.close();
                if (client != null && !client.isClosed()) client.close();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}