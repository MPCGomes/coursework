package socketslista01;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;



public class ServidorMultiCliente {

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(12345);

        System.out.println("Porta 12345 aberta! Aguardando conexão...");

       while(true){
        Socket cliente = servidor.accept();
        ThreadClienteNovo tcn = new ThreadClienteNovo(cliente);
        tcn.start();
       }

    }
}

