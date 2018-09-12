import java.util.concurrent.atomic.*;

public class Exercises {
    static void exercise2_1_1() {
        int factors = 0;
        for (int i = 0; i < 4999999; i++) 
            factors += TestCountFactors.countFactors(i);

        System.out.println(String.format("Factors were: %d", factors));
    }

    static void exercise2_1_3() {
        final int THREAD_AMOUNT = 10;
        final int AMOUNT = 4999999;
        final Thread[] threads = new Thread[THREAD_AMOUNT];
        final AtomicInteger factors = new AtomicInteger();
        int from = 0;
        int to = AMOUNT/THREAD_AMOUNT;

        // System.out.println("Starting threads.");
        for (int i = 0; i < threads.length; i++) {
            int fromCopy = from;
            int toCopy = to;
            // int iCopy = i+1;
            threads[i] = new Thread(() -> {
                // System.out.println("Starting thread " + iCopy + " from " + fromCopy + " to " + toCopy);
                for (int f = fromCopy; f < toCopy; f++) {
                    factors.addAndGet(TestCountFactors.countFactors(f));
                }
            });
            from = to + 1;
            to = from + AMOUNT / THREAD_AMOUNT;
            threads[i].start();
        }

        // System.out.println("Attempting to join threads.");
        for (int i = 0; i < threads.length; i++) {
            try {
                // System.out.println("Joining thread " + (i+1));
                threads[i].join();
            } catch (Exception e) { 
                System.out.println("Got exception: " + e.getLocalizedMessage() + " when trying to join threads.");
            }
        }

        System.out.println(String.format("Result was: %d", factors.get()));
    }

    private static void testHistogram(Histogram histogram, int threads) {
        final int RANGE = 4999999;
        int from = 0;
        int share = RANGE/threads;
        Thread[] threadArray = new Thread[threads];

        for (int t = 1; t <= threads; t++) {
            int fromCopy = from; 
            int to = share*t;
                Thread th = new Thread(() -> {
                    for (int i = fromCopy; i < to; i++) {
                        histogram.increment(TestCountFactors.countFactors(i));
                    }});
                threadArray[t-1] = th;
                th.start();
            from = share * t;
        }

        for (Thread th : threadArray) {
            try { th.join(); } catch (Exception e) { }
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("bin[" + i + "] is: " + histogram.getCount(i));
        }
    }

    public static void exercise_2_3_2() {
        final int RANGE = 4999999;
        final Histogram histogram = new Histogram2(RANGE);
        testHistogram(histogram, 4);
    }

    public static void exercise_2_3_3() {
        final int RANGE = 4999999;
        final Histogram histogram = new Histogram3(RANGE);
        testHistogram(histogram, 4);
    }

    public static void exercise_2_3_4() {
        final int RANGE = 4999999;
        final Histogram histogram = new Histogram4(RANGE);
        testHistogram(histogram, 4);
    }

    public static void exercise_2_3_6() {
        final int RANGE = 4999999;
        final Histogram histogram = new Histogram5(RANGE);
        testHistogram(histogram, 4);
    }

    public static void main(String[] args) {
        exercise_2_3_6();
    }
}