
ANSWERS
==========

Emma Arfelt Kock, ekoc
Anders Fischer, afin

_Implementation exercises can be found in either their original files as extensions to existing classes, or in Exercises.java._

Exercise 2.1
------------
### 2.1.1
Sequential result:     664579
java TestCountPrimes  6.89s user 0.05s system 99% cpu 6.939 total

### 2.1.2 
Parallel10N  result:     664579
java TestCountPrimes  10.40s user 0.06s system 586% cpu 1.785 total

### 2.2.3
Parallel2  result:     663725
java TestCountPrimes  7.04s user 0.06s system 158% cpu 4.466 total

This result is not correct. We encounter a lost-update as threads can read old values of the count field. 

### 2.2.4
In theory it does not matter, since we do not call `.get()` from the runnable. It would matter, if we used the `.get()` method to get the old value and manipulate it. 


Exercise 2.2
------------

### 2.2.1 
`time java Exercises` outputs:
6.96s user 0.07s system 100% cpu 7.004 total

###  2.2.2
```java
    class MyAtomicInteger {
    private int value = 0;

    public int addAndGet(int amount) {
        synchronized (this) {
            value += amount;
            return value;
        }
    }

    public int get() {
        synchronized (this) {
            return value;
        }
    }
}
```

See MyAtomicInteger.java

###  2.2.3
`time java Exercises` outputs:
11.90s user 0.07s system 366% cpu 3.266 total

###  2.2.4
No, the `addAndGet()`-method would not give the expected result, since the increment (+=) operator is not a single atomic operation.

We got varying result using a non-synchronized addAndGet call with a volatile private int value, demonstrating that the AtomicInteger isn't atomic. 

###  2.2.5
There is a noticeable difference. The built-in class is faster.  
`time java Exercises` outputs:
10.48s user 0.07s system 361% cpu 2.923 total

I would say that making the field static would be a good idea, since that ensures that other threads don't see the AtomicInteger in a partially constructed state. 


Exercise 2.3
------------

### 2.3.1
`cache` needs to be declared volatile in order for the changes to be visible across threads.

### 2.3.2
`final` ensures that the members of the instance are set before other threads can access these. Therefore this is required since the members are used `getFactors()``


Exercise 2.4
------------

### 2.4.1
We've added syncronized blocks inside the methods of Histogram2 except for `getSpan`. The `counts` array has also been made final, since it should never be modified after instantiation, and we want to make sure that it has been instantiated before other threads modify the array. 
`getSpan` does not need to be synchronized since the length of the array never changes after instantiation of the class.

### 2.4.2
See SimpleHistogram.java. 

### 2.4.3
Yes, we can remove all `synchronized` since the `AtomicInteger` class is thread safe. 

### 2.4.4 & 5 & 6
See SimpleHistogram.java, results were correct. 


Exercise 2.5
------------

### 2.5.1
```java 
private static void exerciseFactorizer(Computable<Long, long[]> f) {
    final int threadCount = 16;
    final long start = 10_000_000_000L, range = 20_000L;
    
    Thread[] threads = new Thread[threadCount];
    for (int t=0; t<threadCount; t++) {
      long from1 = start, to1 = from1+range, from2 = start+range+t*range/4, to2 = from2+range;
      threads[t] = new Thread(() -> {
        try {
          for (long i=from1; i<to1; i++)
            f.compute(i);

          for(long i = from2; i <to2; i++)
            f.compute(i);
        } catch (InterruptedException e) {e.getMessage();}
      });
    }
    for (int t=0; t<threadCount; t++) 
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
  }
```

### 2.5.2
Result: 115000
java TestCache  19.12s user 0.94s system 106% cpu 18.860 total
_We assumed that the assignment meant `memorize1` instead of `memorize0` as stated in the assignment, since only `memorize1` existed at this point_

### 2.5.3
Memorizer2: It is called 163129 times, since threads are not aware that another thread is "working" already and therefore start the same work twice. 
`time` gives `42.99s user 0.24s system 335% cpu 12.865 total`

### 2.5.4
Memorizer3: It is called 116659 times, since threads are not aware that another thread is "working" already and therefore start the same work twice. 
`time` gives `27.86s user 2.25s system 334% cpu 9.008 total`

### 2.5.5
Memorizer4: It is called 115000 times. Since putIfAbsent is being used, the window of opportunity for another thread to start the same work is gone. 
`time` gives `25.86s user 1.48s system 250% cpu 10.916 total`

### 2.5.6
Memorizer5: It is called 115000 times. 
`time` gives `27.87s user 2.28s system 333% cpu 9.035 total`

### 2.5.7
```java
class Memoizer0<A, V> implements Computable<A, V> {
  private final Map<A, V> cache 
    = new ConcurrentHashMap<A, V>();
  private final Computable<A, V> c;
  
  public Memoizer0(Computable<A, V> c) { this.c = c; }

  public synchronized V computeIfAbsent(final A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = c.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}
```

Exercise 2.6
------------

### 2.6.1
Main finishes, and the two threads have not even "made it" to 30000000 yet.

### 2.6.2
Since we're locking on a `Long` object and the `++` operation behind-the-scenes creates a new lock object by unboxing, adding `1` to the internal long representation, and then boxing the primitive `long` again, we end up with a Long object for each thread. 

Therefore each thread has its own lock object that it increments. If we increase the number of threads, then we "lower" the value that each thread "ends up" with, since they're operating with different locks.

### 2.6.3
See TestStaticCounter.java. We just added a `Object` lock and used it instead of syncing on the `Long` itself. One line added, one line changed. 