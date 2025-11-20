package threads.counter;

public class SynchronizedCounterDemo {

    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();
        int iterationCount = 10000;

        SynchronizedCounterTask taskOne =
                new SynchronizedCounterTask(synchronizedCounter, iterationCount);
        SynchronizedCounterTask taskTwo =
                new SynchronizedCounterTask(synchronizedCounter, iterationCount);

        Thread threadOne = new Thread(taskOne);
        Thread threadTwo = new Thread(taskTwo);

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();

        System.out.println("Final value with synchronization: " + synchronizedCounter.getValue());
    }
}
