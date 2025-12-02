import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ReadData extends Thread {
    private Socket client;
    private Person person;

    public ReadData(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        Scanner input = null;
        PrintStream output = null;

        try {
            input = new Scanner(client.getInputStream());
            output = new PrintStream(client.getOutputStream());

            String name = "";
            String address = "";
            String cpf = "";
            String age = "";
            String height = "";
            String birthDate = "";

            output.println("Enter person's name: ");
            if (input.hasNextLine()) {
                name = input.nextLine();
            }

            output.println("Enter person's address: ");
            if (input.hasNextLine()) {
                address = input.nextLine();
            }

            output.println("Enter person's CPF: ");
            if (input.hasNextLine()) {
                cpf = input.nextLine();
            }

            output.println("Enter person's age: ");
            if (input.hasNextLine()) {
                age = input.nextLine();
            }

            output.println("Enter person's height: ");
            if (input.hasNextLine()) {
                height = input.nextLine();
            }

            output.println("Enter person's birth date: ");
            if (input.hasNextLine()) {
                birthDate = input.nextLine();
            }

            person = new Person(name, address, cpf, age, height, birthDate);

            System.out.println("Data received from client " + client.getInetAddress().getHostAddress());
            System.out.println("Name: " + name);
            System.out.println("Address: " + address);
            System.out.println("CPF: " + cpf);
            System.out.println("Age: " + age);
            System.out.println("Height: " + height);
            System.out.println("Birth Date: " + birthDate);

            SaveData saveData = new SaveData(person);
            saveData.start();
            saveData.join();

            output.println("Data saved successfully!");
            System.out.println("Client " + client.getInetAddress().getHostAddress() + " finished\n");

        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing client: " + e.getMessage());
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (client != null && !client.isClosed()) client.close();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public Person getPerson() {
        return person;
    }
}