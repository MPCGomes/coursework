package threads.counter;

public class CounterRaceTask implements Runnable {

    private final Counter counter;
    private final int iterationCount;

    public CounterRaceTask(Counter counter, int iterationCount) {
        this.counter = counter;
        this.iterationCount = iterationCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterationCount; i++) {
            counter.increment();
        }
    }
}
