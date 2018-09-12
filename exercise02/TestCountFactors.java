// For week 2
// sestoft@itu.dk * 2014-08-29
import java.util.concurrent.atomic.*;


class TestCountFactors {
  public static void main(String[] args) {
    final int range = 5_000_000;
    int count = 0;

    //System.out.printf("Sequential result: %10d%n%n", countSequential(range));
    System.out.printf("Parallel 10 threads result: %10d%n%n", countParallel10()); 
  }

  public static int countFactors(int p) {
    if (p < 2) 
      return 0;
    int factorCount = 1, k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
	      factorCount++;
	      p /= k;
      } else 
	    k++;
    }
    return factorCount;
  }

  public static int countSequential(int range) {
    int count = 0; 
    for (int p=0; p<range; p++)
      count += countFactors(p);
    return count;
  }

  /* Using Java's atomicInteger */
  private static long countParallel10() {
    final int perThread = 4_999_999 / 10;
    AtomicInteger counter = new AtomicInteger(0);

    Thread[] threads = new Thread[10];
    for (int t=0; t<10; t++) {
      final int from = perThread * t, 
        to = (t+1==10) ? 4_999_999 : perThread * (t+1); 
      threads[t] = new Thread(() -> {
        for (int i=from; i<to; i++)
          counter.addAndGet(countFactors(i));
      });
    }
    for (int t=0; t<10; t++) 
      threads[t].start();
    try {
      for (int t=0; t<10; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
    return counter.get();
  }


/*
*
* Implements 'MyAtomicInteger'
* 

  private static long countParallel10() {
    final int perThread = 4_999_999 / 10;
    final MyAtomicInteger counter = new MyAtomicInteger(0);

    Thread[] threads = new Thread[10];
    for (int t=0; t<10; t++) {
      final int from = perThread * t, 
        to = (t+1==10) ? 4_999_999 : perThread * (t+1); 
      threads[t] = new Thread(() -> {
        for (int i=from; i<to; i++)
          counter.addAndGet(countFactors(i));
      });
    }
    for (int t=0; t<10; t++) 
      threads[t].start();
    try {
      for (int t=0; t<10; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
    return counter.get();
  }
*/

}

class MyAtomicInteger {
  private int value; 

  public MyAtomicInteger(int value) { this.value = value;}

  public synchronized int addAndGet(int amount) {
    value += amount;   
    return value;
  }

  public int get() {
    return value; 
  }
}
