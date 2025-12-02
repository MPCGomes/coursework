package socketslista01;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ThreadClienteNovo extends Thread {

    Socket cliente;

    public ThreadClienteNovo(Socket cliente) {
        this.cliente = cliente;
    }
    
    public void run(){
        try {
            System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
            System.out.println("Esse socket ficará aberto até que o cliente se desconecte");
            
            Scanner entrada = new Scanner(cliente.getInputStream());
            ThreadReceptor rc = new ThreadReceptor(entrada);
            rc.start();
            
            Scanner teclado = new Scanner(System.in);
            
            PrintStream saida = new PrintStream(cliente.getOutputStream());
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            
            System.out.print("Você é o SERVIDOR, let the game begin !\n");
            
            while (teclado.hasNextLine()) {
                Date hora = Calendar.getInstance().getTime();
                String dataFormatada = sdf.format(hora);
                
                saida.println("Servidor ("+dataFormatada+"): " + teclado.nextLine());
            }
            
            teclado.close();
            entrada.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadClienteNovo.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
