import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.LongAdder;

// For week 2
// sestoft@itu.dk * 2014-09-04

class SimpleHistogram {
  public static void main(String[] args) {
   /* final Histogram histogram = new Histogram1(30);
    histogram.increment(7);
    histogram.increment(13);
    histogram.increment(7);
    dump(histogram);
   */
    countPrimeInRange();

  }

  public static void dump(Histogram histogram) {
    int totalCount = 0;
    for (int bin=0; bin<histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
      totalCount += histogram.getCount(bin);
    }
    System.out.printf("      %9d%n", totalCount);
  }

  public static void countPrimeInRange() {
    final int perThread = 4_999_999 / 4;
    final Histogram4 hist = new Histogram4(perThread);

    Thread[] threads = new Thread[4];
    for (int t=0; t<4; t++) {
      final int from = perThread * t, 
        to = (t+1==4) ? 4_999_999 : perThread * (t+1); 
      threads[t] = new Thread(() -> {
        for (int i=from; i<to; i++)
          hist.increment(TestCountFactors.countFactors(i)); 
      });
    }
    for (int t=0; t<4; t++) 
      threads[t].start();
    try {
      for (int t=0; t<4; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
   
    for(int i = 0; i < 10; i++) {
      System.out.println("bin[" + i + "]: " + hist.getCount(i));
    }

  }
}

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
  public int[] getBins();
}

class Histogram1 implements Histogram {
  private int[] counts;
  public Histogram1(int span) {
    this.counts = new int[span];
  }
  public void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }

  public int[] getBins() { return counts.clone();}
}

class Histogram2 implements Histogram {
  private final int[] counts;

  public Histogram2(int span) {
    this.counts = new int[span];
  }
  public synchronized void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public synchronized int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }

  public int[] getBins() {
    return counts.clone();
  }
}

class Histogram3 implements Histogram {
  private final AtomicInteger[] counts;

  public Histogram3(int span) {
    this.counts = new AtomicInteger[span];
    for(int i = 0; i<counts.length; i++) {
      counts[i] = new AtomicInteger();
    }
  }
  public void increment(int bin) {
    counts[bin].incrementAndGet();
  }

  public int getCount(int bin) {
    return counts[bin].get();
  }
  public int getSpan() {
    return counts.length;
  }

  public int[] getBins() {
    int[] bins = new int[counts.length];
    for(int i = 0; i < counts.length; i++) {
      bins[i] = counts[i].get();
    }
    return bins; 
  }

}

class Histogram4 implements Histogram {
  private final AtomicIntegerArray counts;

  public Histogram4(int span) {
    this.counts = new AtomicIntegerArray(new int[span]);
  }

  public void increment(int bin) {
    counts.incrementAndGet(bin);
  }

  public int getCount(int bin) {
    return counts.get(bin);
  }
  public int getSpan() {
    return counts.length();
  }

  public int[] getBins() {
    int[] bins = new int[counts.length()];
    for(int i = 0; i < counts.length(); i++) {
      bins[i] = counts.get(i);
    }
    return bins; 
  }

  class Histogram5 implements Histogram {
    private LongAdder[] counts;

    public Histogram5(int span) {
      this.counts = new LongAdder[span];
    }
    public void increment(int bin) {
      counts[bin].increment();
    }

    public int getCount(int bin) {
      return counts[bin].intValue();
    }
    public int getSpan() {
      return counts.length;
    }

    public int[] getBins() { 
      int[] bins = new int[counts.length];
      for(int i = 0; i < counts.length; i++) {
        bins[i] = counts[i].intValue();
      }
      return bins; 
    }
  }

}
