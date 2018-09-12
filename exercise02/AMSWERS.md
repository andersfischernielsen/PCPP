
ANSWERS
==========


Exercise 2.1
------------

### 2.1.1 
6.96s user 0.07s system 100% cpu 7.004 total

###  2.1.2
See MyAtomicInteger.java

###  2.1.3
`time java Exercises` outputs:
11.90s user 0.07s system 366% cpu 3.266 total

###  2.1.4
No, the addAndGet()-method would not give the expected result, since the increment (+=) operator is not a single atomic operation.
We got varying result using a non-synchronized addAndGet call with a volatile private int value, demonstrating that the AtomicInteger isn't atomic. 

###  2.1.5
There is a noticeable difference. The built-in class is faster.  
`time java Exercises` outputs:
10.48s user 0.07s system 361% cpu 2.923 total

I would say that making the field static would be a good idea, since that ensures that other threads don't see the AtomicInteger in a partially constructed state. 


Exercise 2.2
------------

### 2.2.1
`cache` needs to be declared volatile in order for the changes to be visible across threads.

### 2.2.2
`final` ensures that the members of the instance are set before other threads can access these. Therefore this is required since the members are used `getFactors()``


Exercise 2.3
