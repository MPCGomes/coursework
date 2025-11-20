package threads.vectorsum;

public class VectorSumTask implements Runnable {

    private final int[] arrayOne;
    private final int[] arrayTwo;
    private final int[] arrayThree;
    private final int[] arrayFour;
    private final int[] arraySum;
    private final int start;
    private final int end;

    public VectorSumTask(
            int[] arrayOne,
            int[] arrayTwo,
            int[] arrayThree,
            int[] arrayFour,
            int[] arraySum,
            int start,
            int end
    ) {
        this.arrayOne = arrayOne;
        this.arrayTwo = arrayTwo;
        this.arrayThree = arrayThree;
        this.arrayFour = arrayFour;
        this.arraySum = arraySum;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            arraySum[i] = arrayOne[i] + arrayTwo[i] + arrayThree[i] + arrayFour[i];
        }
    }
}
