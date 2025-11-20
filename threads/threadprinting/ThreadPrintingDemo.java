package threads.threadprinting;

import java.util.Scanner;

public class ThreadPrintingDemo {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of threads: ");
        int threadCount = scanner.nextInt();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int threadNumber = i + 1;

            threads[i] = new Thread(() -> {
                System.out.println("I am thread number " + threadNumber);
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        scanner.close();
    }
}
