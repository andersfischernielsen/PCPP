ANSWERS
==========

_Emma Arfelt Kock, ekoc_

_Anders Fischer, afin_

# Exercise 7

## Exercise 7.1

### 7.1.1

The documentation at: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html

"The programmer does not need to provide code that explicitly creates these threads: they are provided by the runtime or the Swing framework. The programmer's job is to utilize these threads to create a responsive, maintainable Swing program.

This lesson discusses each of the three kinds of threads in turn. Worker threads require the most discussion because tasks that run on them are created using javax.swing.SwingWorker. This class has many useful features, including communication and coordination between worker thread tasks and the tasks on other threads." 

Therefore we should use multiple SwingWorkers and not delegate work using an executor in a single SwingWorker. 

### 7.1.2