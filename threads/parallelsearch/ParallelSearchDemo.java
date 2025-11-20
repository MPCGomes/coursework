package threads.parallelsearch;

import java.util.Random;

public class ParallelSearchDemo {

    public static void main(String[] args) throws InterruptedException {
        int length = 100000;
        int[] array = new int[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(51);
        }

        int threadCount = 100;
        int blockSize = length / threadCount;
        int target = 17;

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int start = i * blockSize;
            int end = (i == threadCount - 1) ? length : start + blockSize;

            ParallelSearchTask task = new ParallelSearchTask(array, start, end, target);

            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
