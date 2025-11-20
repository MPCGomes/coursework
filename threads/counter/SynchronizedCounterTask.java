package threads.counter;

public class SynchronizedCounterTask implements Runnable {

    private final SynchronizedCounter synchronizedCounter;
    private final int iterationCount;

    public SynchronizedCounterTask(SynchronizedCounter synchronizedCounter, int iterationCount) {
        this.synchronizedCounter = synchronizedCounter;
        this.iterationCount = iterationCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterationCount; i++) {
            synchronizedCounter.increment();
        }
    }
}
