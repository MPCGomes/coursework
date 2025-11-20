package threads.parallelsearch;

public class ParallelSearchTask implements Runnable {

    private final int[] array;
    private final int start;
    private final int end;
    private final int target;

    public ParallelSearchTask(int[] array, int start, int end, int target) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.target = target;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                System.out.println("Value " + target + " found at position " + i);
            }
        }
    }
}
