
ANSWERS
==========

_Implementation exercises can be found in either their original files as extensions to existing classes, or in Exercises.java._

Exercise 2.2
------------

### 2.2.1 
6.96s user 0.07s system 100% cpu 7.004 total

###  2.2.2
See MyAtomicInteger.java

###  2.2.3
`time java Exercises` outputs:
11.90s user 0.07s system 366% cpu 3.266 total

###  2.2.4
No, the addAndGet()-method would not give the expected result, since the increment (+=) operator is not a single atomic operation.

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
See Exercises.java. 

### 2.4.3
Yes, we can remove all `synchronized` since the `AtomicInteger` class is thread safe. 

### 2.4.4 & 5 & 6
See Exercises.java, results were correct. 


Exercise 2.5
------------


Exercise 2.6
------------

### 2.6.1
Main finishes, and the two threads have not even "made it" to 30000000 yet.

### 2.6.2
Since we're locking on a `Long` object and the `++` operation behind-the-scenes creates a new lock object by unboxing, adding `1` to the internal long representation, and then boxing the primitive `long` again, we end up with a Long object for each thread. 

Therefore each thread has its own lock object that it increments. If we increase the number of threads, then we "lower" the value that each thread "ends up" with, since they're operating with different locks.

### 2.6.3
See TestStaticCounter.java. We just added a `Object` lock and used it instead of syncing on the Long itself. One line added, one line changed. 