package socketslista01;

import java.util.Scanner;

public class ThreadReceptor extends Thread {

    Scanner entrada;

    public ThreadReceptor(Scanner entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        while (entrada.hasNextLine()) {
            String mensagem = entrada.nextLine();
            System.out.println(">> " + mensagem);
        }
    }
}
