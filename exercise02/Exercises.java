import java.util.concurrent.atomic.*;

public class Exercises {
    static void exercise2_1() {
        int factors = 0;
        for (int i = 0; i < 4999999; i++) 
            factors += TestCountFactors.countFactors(i);

        System.out.println(String.format("Factors were: %d", factors));
    }

    static void exercise2_3() {
        final int THREAD_AMOUNT = 10;
        final int AMOUNT = 4999999;
        Thread[] threads = new Thread[THREAD_AMOUNT];
        final MyAtomicInteger factors = new MyAtomicInteger();
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

    public static void main(String[] args) {
        exercise2_3();
    }
}