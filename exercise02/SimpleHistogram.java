import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.LongAdder;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;

// For week 2
// sestoft@itu.dk * 2014-09-04

class SimpleHistogram {
  public static void main(String[] args) {
    final Histogram histogram = new Histogram1(30);
    histogram.increment(7);
    histogram.increment(13);
    histogram.increment(7);
    dump(histogram);
  }

  public static void dump(Histogram histogram) {
    int totalCount = 0;
    for (int bin=0; bin<histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
      totalCount += histogram.getCount(bin);
    }
    System.out.printf("      %9d%n", totalCount);
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
  public int[] getBins() {
    return null;
  }
}

class Histogram2 implements Histogram {
  private final int[] counts;

  public Histogram2(int span) {
    this.counts = new int[span];
  }
  public void increment(int bin) {
    synchronized (this) {
      counts[bin] = counts[bin] + 1;
    }
  }
  public int getCount(int bin) {
    synchronized (this) {
      return counts[bin];
    }
  }
  public int getSpan() {
    return counts.length;
  }
  public int[] getBins() {
    //This returns a snapshot of the array. 
    return Arrays.copyOf(counts, counts.length); 
  }
}

class Histogram3 implements Histogram {
  private final AtomicInteger[] counts;
  private final Object lock = new Object();

  public Histogram3(int span) {
    this.counts = new AtomicInteger[span];
    Arrays.setAll(this.counts, (i) -> new AtomicInteger());
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
      //We're unsure if .stream() and .map() are thread safe, 
      //so therefore lock.
      //Returns a snapshot.
      synchronized (lock) { 
        return Arrays.stream(counts)
          .mapToInt((i) -> i.get())
          .toArray();
    }
  }
}

class Histogram4 implements Histogram {
  private final AtomicIntegerArray counts;

  public Histogram4(int span) {
    this.counts = new AtomicIntegerArray(span);
  }
  public void increment(int bin) {
    counts.addAndGet(bin, 1);
  }
  public int getCount(int bin) {
    return counts.get(bin);
  }
  public int getSpan() {
    return counts.length();
  }
  public int[] getBins() { //Snapshot.
      int length = counts.length();
      int[] result = new int[length];
      for (int i = 0; i < length; i++) {
        result[i] = counts.get(i);
      }
      return result;
  }
}

class Histogram5 implements Histogram {
  private final LongAdder[] counts;
  private final Object lock = new Object();

  public Histogram5(int span) {
    this.counts = new LongAdder[span];
    Arrays.setAll(this.counts, (i) -> new LongAdder());
  }
  public void increment(int bin) {
    counts[bin].add(1);
  }
  public int getCount(int bin) {
    return counts[bin].intValue();
  }
  public int getSpan() {
    return counts.length;
  }
  public int[] getBins() {
      //We're unsure if .stream() and .map() are thread safe, 
      //so therefore lock.
      //Returns a snapshot.
      synchronized (lock) { 
      return Arrays.stream(counts)
        .mapToInt((i) -> i.intValue())
        .toArray();
    }
  }
}