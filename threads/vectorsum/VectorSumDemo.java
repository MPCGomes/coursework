package threads.vectorsum;

import java.util.Random;

public class VectorSumDemo {

    public static void main(String[] args) throws InterruptedException {
        int length = 10000;

        int[] arrayOne = new int[length];
        int[] arrayTwo = new int[length];
        int[] arrayThree = new int[length];
        int[] arrayFour = new int[length];
        int[] arraySum = new int[length];

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            arrayOne[i] = random.nextInt(10);
            arrayTwo[i] = random.nextInt(10);
            arrayThree[i] = random.nextInt(10);
            arrayFour[i] = random.nextInt(10);
        }

        int threadCount = 4;
        Thread[] threads = new Thread[threadCount];
        int blockSize = length / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int start = i * blockSize;
            int end = (i == threadCount - 1) ? length : start + blockSize;

            VectorSumTask task = new VectorSumTask(
                    arrayOne,
                    arrayTwo,
                    arrayThree,
                    arrayFour,
                    arraySum,
                    start,
                    end
            );

            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (int i = 0; i < 20; i++) {
            System.out.println(
                    arrayOne[i] + "+" +
                            arrayTwo[i] + "+" +
                            arrayThree[i] + "+" +
                            arrayFour[i] + " = " +
                            arraySum[i]
            );
        }
    }
}
