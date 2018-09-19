
ANSWERS
==========

Emma Arfelt Kock, ekoc
Anders Fischer, afin


Exercise 3.1
------------
### 3.1.1
See res_31.txt for results and system info. 
The results do not differ enormously from the Microbenchmarks report's results. 

### 3.1.2
See res_312.txt for results and system info. 
The standard diviation is much larger on Computer (1), then what is shown in Microbenchmarks section 4.2. This is mostly likely due to the fact, that there was several programs running in the background, that could not be shut down. E.g. system updates, wifi etc. 


Exercise 3.2
------------

### 3.2.1 


Exercise 3.3
------------

### 3.3.1
See res_331.txt for results and system info. 

### 3.3.2
See attached

### 3.3.3
See attached 

### 3.3.4
No, they are the same. 

### 3.3.5
```java
 threads[t] = new Thread( () -> {
                long count = 0
                for (int i=from; i<to; i++)
                    if (isPrime(i))
                       count++;
                lc.getAndAdd(count);
            });
```
This is not faster on Computer 1. 

Exercise 3.4
------------

### 3.4.1
