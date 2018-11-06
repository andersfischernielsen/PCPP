# ANSWERS

_Emma Arfelt Kock, ekoc_

_Anders Fischer, afin_

# Exercise 9

## Exercise 9.2

### 9.2.1

```java
class StmHistogram implements Histogram {
  private final TxnInteger[] counts;

  public StmHistogram(int span) {
    this.counts = new TxnInteger[span];
    for (int i = 0; i < span; i++) {
      this.counts[i] = newTxnInteger(0);
    }
  }

  public void increment(int bin) {
    atomic(() -> counts[bin].increment());
  }

  public int getCount(int bin) {
    return atomic(() -> counts[bin].get());
  }

  public int getSpan() {
    return atomic(() -> counts.length);
  }
```

### 9.2.2

Yes, it produces the same result.

### 9.2.3

```java
 public int[] getBins() {
    return atomic(() -> {
      int[] bins = new int[counts.length];
      for (int i = 0; i < counts.length; i++) {
        bins[i] = this.getCount(i);
      }
      return bins;
    });
  }
```

### 9.2.4

```java
public int getAndClear(int bin) {
    return atomic(() -> {
      int c = getCount(bin);
      counts[bin] = newTxnInteger(0);
      return c;
    });
  }
```

### 9.2.5

```java
     public void transferBins(Histogram hist) {
    atomic(() -> {
      int[] bins = hist.getBins();
      for (int currentBin = 0; currentBin < bins.length; currentBin++) {
        int amount = hist.getAndClear(currentBin);
        this.counts[currentBin].increment(amount);
      }
    });
  }
```

### 9.2.6

```java
  final Histogram total = new StmHistogram(30);
  ...
  for (int i = 0; i < 200; i++) {
      total.transferBins(histogram);
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
      }
```

### 9.2.7

We would expect the Histograms to have traded contents, since that is what the specification of `transferBins` specifies.

## 9.3

### 9.3.1

### 9.3.2

### 9.3.3

### 9.3.4

### 9.3.5
