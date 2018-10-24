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

Alterations in the code (Class DownloadWorker):
```java 
 public String doInBackground() {
      StringBuilder sb = new StringBuilder();
      int count = 0;
        if (isCancelled())			    // (3)
           return sb.toString();
	      System.out.println("Fetching " + url);
        String page = getPage(url, 200);
        String result = String.format("%-40s%7d%n", url, page.length());
        sb.append(result); // (1)
        setProgress((100 * ++count) / urls.length); // (2)
        publish(result); // (4)
        textArea.append(result);
        return sb.toString();
    }
```
and in Class TestFetchWebGui: 
```java 
    // (1) Use a background thread, not the event thread, for work
    List<DownloadWorker> downloadWorkers = new ArrayList<DownloadWorker>();
    for (String url : urls) {
      downloadWorkers.add(new DownloadWorker(textArea, url));
    }

    fetchButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          for (DownloadWorker d : downloadWorkers)
          d.execute();
        }});

```

### 7.1.2
```java
 // (3) Enable cancellation
    cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          for (DownloadWorker downloadTask : downloadWorkers) {
            downloadTask.cancel(false);
          }
        }});
```

### 7.1.3
Alteration in Class TestFetchWebGui (in method goodFetch()): 

```java
 JProgressBar progressBar = new JProgressBar(0, 100);
    //    progressBar.setValue(50);
    outerPanel.add(progressBar, BorderLayout.SOUTH);

    for (DownloadWorker downloadTask : downloadWorkers) {
      downloadTask.addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
          if ("progress".equals(e.getPropertyName())) {
            int count = (Integer)e.getNewValue();
            progressBar.setValue(100 * count / downloadWorkers.size());
          }}});
    }
```

and 
```java
private static final AtomicInteger count = new AtomicInteger(1);
...
public String doInBackground() {
    ...
    setProgress(count.getAndIncrement()); // (2)
    ...
```

## 7.2
