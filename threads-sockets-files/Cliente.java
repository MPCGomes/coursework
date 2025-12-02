package socketslista01;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws UnknownHostException, IOException {

        Socket cliente = new Socket("127.0.0.1", 12345);

        System.out.println("O cliente se conectou ao servidor!");

        Scanner entrada = new Scanner(cliente.getInputStream());
        ThreadReceptor rc = new ThreadReceptor(entrada);
        rc.start();

        Scanner teclado = new Scanner(System.in);

        PrintStream saida = new PrintStream(cliente.getOutputStream());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        System.out.print("Você é o CLIENTE, let the game begin !\n");

        while (teclado.hasNextLine()) {
            Date hora = Calendar.getInstance().getTime();
            String dataFormatada = sdf.format(hora);

            saida.println("Cliente ("+dataFormatada+"): " + teclado.nextLine());
        }

        teclado.close();
        saida.close();
    }
}
