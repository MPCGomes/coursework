package threads.counter;

public class CounterRaceDemo {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        int iterationCount = 10000;

        CounterRaceTask taskOne = new CounterRaceTask(counter, iterationCount);
        CounterRaceTask taskTwo = new CounterRaceTask(counter, iterationCount);

        Thread threadOne = new Thread(taskOne);
        Thread threadTwo = new Thread(taskTwo);

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();

        System.out.println("Final value without synchronization: " + counter.getValue());
    }
}
