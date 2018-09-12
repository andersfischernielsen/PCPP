
ANSWERS
==========
Emma Arfelt Kock, ekoc
Anders Fischer, afin

Exercise 1.1
------------

### 1.1.1
We get result like "Count is 100534081 and should be 200000000" since running without synchronization makes the threads overwrite eachother's results, thereby having lost updates. 

### 1.1.2
With a smaller amount of operations (100) the chance of the threads interfering with each other is smaller.

I would not consider this software correct, since we can't guarantee anything in regards to order of execution when running concurrent code without synchronization.  

After running the example many times with counts=200, I finally got "Count is 384 and should be 400" illustrating that the same could happen with counts=100, even though the chance of this happening is even smaller, there's still a chance.  

### 1.1.3
No, changing the increment to either += or ++ would not change the erronous result, since these operations aren't atomic either, just like count = count + 1. 

Running with ++ we get smaller counts (no larger than 30 in our tests).

Running with += we get similar results as when running with count = count + 1. 

### 1.1.4
We don't know what the final value should be, since the code is not synchronised. 
The code suggests that the result should be 0, but since there's no synchronization, this cannot be guaranteed.
We will still lose updates and/or read stale data without synchronisation. 
Tests show results anywhere between "Count is -8802343 and should be 0" and "Count is 9763543 and should be 0".

### 1.1.5
(i) Running with only decrement being synchronised does not change the result from when running completely without synchronisation.

(ii) Running with only increment being synchronised does not change the result from when running completely without synchronisation.
Values for both of these are anywhere between "Count is 1824261 and should be 0" and "Count is -8469785 and should be 0".

(iii) Running with both increment and decrement synchronised gives the expected result 0. 

(iv) In the experiments where either increment or decrement isn't synchronised we still have reads og stale data or lost updates on either operation, resulting in the same results as running completely without synchronization. 
Race conditions therefore still occur.



Exercise 1.2
------------

### 1.2.1
Both threads can call the print method simultaneously, thereby printing symbols in the "wrong order".
In the scenario where one thread is sleeping and has just printed a dash or a line and the other thread then prints either of the two symbols, a double dash or line will occur. 

The answer is also stated in the exercise as: _"(...)  this phenomenon can be caused only by one thread printing a bar and then the other thread printing a bar before the first one gets to print its dash."_

### 1.2.2 
Making the print method synchronous would prevent either thread from printing while the other thread is printing.

### 1.2.3 &  1.2.4
```java
    public class Printer {
        public static void print() {
            synchronized (Printer.class) {
                System.out.print("-");
                try { Thread.sleep(50);
                } catch (InterruptedException exn) { }
                
                System.out.print("|");
            }
        }

        public static void main(String[] args) {
            //Printer p = new Printer(); for exercise 1.2.3
            Thread t1 = new Thread(() -> {
            while(true) Printer.print();
            });

            Thread t2 = new Thread(() -> {
                while(true) Printer.print();
            });

            t1.start(); 
            t2.start();

        }
    }
```



Exercise 1.3
------------

### 1.3.1
Yes, we observe the same behaviour. 

### 1.3.2
Yes, the thread terminates as expected now. 

### 1.3.3
No, it does not terminate as expected. 

Furthermore, if the get method is not synchronised, then it's still possible to get() the value before it is initialized, and therefore the thread will loop forever. 


### 1.3.4
The volatile field modifier is used to ensure visibility (but not mutual exclusion), so we should not trust this entirely. Locking can guarantee both visibility and atomicity; 
volatile variables can only guarantee visibility.

So the calls to `get()` and `set()` could still lead to a race condition. 


Exercise 1.4
------------

### 1.4.1
````
Sum is 1790827,000000 and should be 2000000,000000
Sum is 1864322,000000 and should be 2000000,000000
Sum is 1874701,000000 and should be 2000000,000000
Sum is 1783576,000000 and should be 2000000,000000
````
The Mystery class appears to be non-thread-safe.

### 1.4.2
When locking on a static method, the locking "happens on" the `Class` object of the instance. Therefore the lock is the `Class` object, and not the instance object.

Since we're calling both static and instance methods, the final result will be less than the expected value due to inconsistent locking when locking on the both `Class` and instance object..

### 1.4.3
We would not change the `Mystery` class, and instead consistently use either the static methods to ensure that the same lock is being used consistently.



Exercise 1.5
------------

### 1.5.1
We would encapsulate the handling of the internal array representation so every call to add(), get() etc. would lock the internal representation in order to make sure that adding and removing elements happen atomically, by using the `synchronized` keyword.

```java
    public synchronized double get(int i) {
            ^^^^^^^^^^
```

### 1.5.2
If using the above method of ensuring thread safety, then the collection would not be very efficient with many threads calling `get()` etc., since every operation would lock the entire collection for all threads except the one currently using/calling on the collection, thereby effectively making the execution synchronous/serial. 

### 1.5.3
(i) _Would this achieve thread-safety?_
It has seperate locks for each operation, and thus a thread can `add()`, while another one can `get()`, and the thread that `get()`s will have lost the update. 

(ii) _Would it achieve visibility? Explain why not._
Since changes to a variable on an object are not visible to other objects while threads are updating the value, the value would not be visible if not set. 

Eg.: _Thread 1_ calls `add()` while _Thread 2_ calls `get()`, then due to different locks being used for different calls, the update by Thread 1 will not be visible to Thread 2. 



Exercise 1.6
------------

### 1.6.1
We would use a lock (on ```this```) and use `synchronized` before incrementing the internal `totalSize` field. 

### 1.6.2
```java
    public DoubleArrayList() {
        synchronized (this) {
            allLists.add(this);
        }
    }
```
If we instantiate the instances with their own lock, then they would never share a lock and each have unique locks. Using this to lock in the constructor ensures that all objects share a lock.


Exercise 1.7
------------

### 1.7.1
The static synchronised call operates on the Class object instead of the instance. Therefore the result is unexpected.

Since `MysteryB` locks on `MysteryB.Class` and likewise for `MysteryA`, `MysteryB` can execute the `increment4` method while `MysteryA` is executing. Therefore we have lost updates, since `MysteryB` (or `MysteryA`) can make durrrrty reads.

### 1.7.2
We would use an inherited instance lock object, and call `synchronized` on that lock object before accessing the internal `count` member.

````java 
class MysteryA {
    private Object lock = new Object();
}

class MysteryB extends MysteryA {

    public whatever() {
        synchronized(lock) {
            ...
        }
    }
}
