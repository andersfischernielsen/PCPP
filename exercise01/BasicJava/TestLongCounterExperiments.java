// For week 1
// sestoft@itu.dk * 2014-08-21

public class TestLongCounterExperiments {
  public static void main(String[] args) {
    final int counts = 10_000_000;
    Thread t1 = new Thread(() -> {
      for (int i=0; i<counts; i++) 
        Printer.print();
      });

    Thread t2 = new Thread(() -> {
      for (int i=0; i<counts; i++) 
      Printer.print();
    });
    
    t1.start(); t2.start();
    try { t1.join(); t2.join(); }
    catch (InterruptedException exn) { 
      System.out.println("Some thread was interrupted");
    }
  
    //   final LongCounter lc = new LongCounter();
  //   final int counts = 10_000_000;
  //   Thread t1 = new Thread(() -> {
  //     for (int i=0; i<counts; i++) 
	// lc.decrement();
  //   });
  //   Thread t2 = new Thread(() -> {
  //     for (int i=0; i<counts; i++) 
	// lc.increment();
  //   });
  //   t1.start(); t2.start();
  //   try { t1.join(); t2.join(); }
  //   catch (InterruptedException exn) { 
  //     System.out.println("Some thread was interrupted");
  //   }
  //   System.out.println("Count is " + lc.get() + " and should be " + 0);
  }
}

class LongCounter {
  private long count = 0;
  public synchronized void increment() {
    count = count+=1;
  }
  public  long get() { 
    return count; 
  }
  public synchronized long decrement() { 
    return count-=1; 
  }
}

class Printer {
  
  public static void print() {
    synchronized (Printer.class) {
      System.out.print("-");
      try { Thread.sleep(50); } catch (InterruptedException exn) { }
      System.out.print("|");
    }
  } 
}