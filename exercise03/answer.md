
ANSWERS
==========

Emma Arfelt Kock, ekoc
Anders Fischer, afin


Exercise 3.1
------------
### 3.1.1


Exercise 3.2
------------

### 3.2.1 
The time for each function call sometimes drops a factor ten for high `count`s, and sometimes the time per operation takes half the time on high `count`s. Generally, the simpler the operation, the faster the speedup. The Thread management benchmarks on avg. speed up to half of the time of the small `count` benchmarks, whereas the simple `Point` operations speed up ten times on high `count`s. 
This is to be expected since the JVM will attempt to speed up common operations, which on high `count`s the JVM will see the function call as.

### 3.2.2
See `res_322.txt` for results.
The results are a bit more precise than Mark6. 

Exercise 3.3
------------

### 3.3.1


Exercise 3.4
------------

### 3.4.1
